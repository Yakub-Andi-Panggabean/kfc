package com.ta.kfc.mercu.interfaces.web.aspect;

import com.ta.kfc.mercu.infrastructure.db.orm.model.security.Menu;
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

    @Autowired
    public AspectController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Pointcut("execution(* *Page(..,org.springframework.ui.Model))")
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
                        .flatMap(Collection::stream).collect(Collectors.toList());

                List<Menu> menus = authorizationService.getAuthorizedMenu().stream()
                        .filter(menu -> !subMenus.contains(menu)).collect(Collectors.toList());

                Optional<Menu> currentMenu = menus.stream().
                        filter(menu -> menu.getPath().
                                equals(request.getServletPath())).findAny();

                List<Menu> currentSubMenus = currentMenu.map(menu -> menu.getChildren())
                        .orElse(Collections.emptyList());

                List<Menu> siblings = menus.stream()
                        .filter(menu -> menu.getChildren()
                                .stream()
                                .anyMatch(child -> child.getPath().equals(request.getServletPath()))
                        )
                        .map(menu -> menu.getChildren())
                        .flatMap(Collection::stream)
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
                    ((Model) arg).addAttribute("username", (String) ((UserDetails) principal).getUsername());
                }


            }
        }
    }

}
