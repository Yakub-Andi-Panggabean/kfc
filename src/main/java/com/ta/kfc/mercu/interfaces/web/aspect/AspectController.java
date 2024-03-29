package com.ta.kfc.mercu.interfaces.web.aspect;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.notification.Notification;
import com.ta.kfc.mercu.infrastructure.db.orm.model.security.Menu;
import com.ta.kfc.mercu.service.notification.NotificationService;
import com.ta.kfc.mercu.service.security.AuthenticationService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Aspect
@Component
public class AspectController {

    private AuthorizationService authorizationService;
    private AuthenticationService authenticationService;
    private NotificationService notificationService;

    @Autowired
    public AspectController(AuthorizationService authorizationService,
                            AuthenticationService authenticationService,
                            NotificationService notificationService) {
        this.authorizationService = authorizationService;
        this.authenticationService = authenticationService;
        this.notificationService = notificationService;
    }

    @Pointcut("execution(* com.ta.kfc.mercu.interfaces.web..*(..,org.springframework.ui.Model))")
    public void pagePointcut() {
    }

    @Before("pagePointcut()")
    public void recoverStateView(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        for (Object arg : args) {
            if (arg instanceof org.springframework.ui.Model) {

                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                List<Menu> subMenus = authorizationService.getAuthorizedMenu().stream()
                        .map(menu -> menu.getChildren())
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

                List<Menu> menus = authorizationService.getAuthorizedMenu().stream()
                        .filter(menu -> !subMenus.contains(menu)).collect(Collectors.toList());

                Optional<Menu> currentMenu = menus.stream().
                        filter(menu -> menu.getPath().
                                equals(request.getServletPath())).filter(m -> m.isEnable())
                        .findAny();

                List<Menu> currentSubMenus = currentMenu.map(menu -> menu.getChildren())
                        .orElse(Collections.emptyList())
                        .stream()
                        .filter(m -> m.isEnable()).collect(Collectors.toList());

                List<Menu> siblings = menus.stream()
                        .filter(menu -> menu.getChildren()
                                .stream()
                                .anyMatch(child -> child.getPath().equals(request.getServletPath()))
                        )
                        .map(menu -> menu.getChildren())
                        .flatMap(Collection::stream)
                        .filter(m -> m.isEnable())
                        .collect(Collectors.toList());

                Optional<Menu> parent = menus.stream().filter(p ->
                        p.getChildren().
                                stream().anyMatch(child ->
                                        child.getPath().equals(request.getServletPath()))).findAny();


                ((Model) arg).addAttribute("menus", menus);
                ((Model) arg).addAttribute("current_path", request.getServletPath());
                ((Model) arg).addAttribute("current_label", parent.map(Menu::getLabel)
                        .orElse(currentMenu.map(Menu::getLabel).orElse("")));


                if (currentSubMenus.isEmpty()) {
                    ((Model) arg).addAttribute("subMenus", siblings);
                } else {
                    ((Model) arg).addAttribute("subMenus", currentSubMenus);
                }

                if (principal instanceof UserDetails) {
                    String username = (String) ((UserDetails) principal).getUsername();
                    ((Model) arg).addAttribute("username", username);

                    Optional<User> user = authenticationService.findUserByUserName(username);
                    if (user.isPresent()) {
                        ((Model) arg).addAttribute("user", user.get());
                        List<Notification> notifications = notificationService.findByUserDetail(user.get());
                        ((Model) arg).addAttribute("unreadNotificationCount", notifications
                                .stream()
                                .filter(n -> !n.isAlreadyRead()).count());

                    }
                }
            }
        }
    }

}
