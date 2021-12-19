package com.ta.kfc.mercu.interfaces.web.notification;

import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.infrastructure.db.orm.model.notification.Notification;
import com.ta.kfc.mercu.service.notification.NotificationService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class NotificationController {

    public static final String NOTIFICATION_PATH = "/notification";

    private AuthorizationService authorizationService;
    private NotificationService notificationService;
    private FastContext context;

    @Autowired
    public NotificationController(AuthorizationService authorizationService,
                                  NotificationService notificationService,
                                  FastContext context) {
        this.authorizationService = authorizationService;
        this.notificationService = notificationService;
        this.context = context;
    }

    @GetMapping({NOTIFICATION_PATH})
    public String getNotificationPage(Model model) {

        List<Notification> notifications = notificationService.findByUserDetail(context.getUser().get());

        model.addAttribute("template", "notification");
        model.addAttribute("notifications", notifications.stream()
                .sorted((n1, n2) -> n2.getCreatedDate().compareTo(n1.getCreatedDate()))
                .collect(Collectors.toList()));

        return "index";
    }

    @GetMapping({NOTIFICATION_PATH + "/{notificationId}"})
    public String getModalNotificationDetailPage(@PathVariable("notificationId") Long id, Model model) {

        Optional<Notification> notification = notificationService.findById(id);

        if (notification.isPresent()) {
            notification.get().setAlreadyRead(true);
            model.addAttribute("notification", notificationService.save(notification.get()).get());
        }

        return "fragments/notification/modal_notification_detail";
    }

}
