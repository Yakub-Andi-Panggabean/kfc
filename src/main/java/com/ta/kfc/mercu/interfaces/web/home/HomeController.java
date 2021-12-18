package com.ta.kfc.mercu.interfaces.web.home;

import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.infrastructure.db.orm.model.notification.Notification;
import com.ta.kfc.mercu.service.notification.NotificationService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private AuthorizationService authorizationService;
    private FastContext context;
    private NotificationService notificationService;

    @Autowired
    public HomeController(AuthorizationService authorizationService, NotificationService notificationService,
                          FastContext context) {
        this.authorizationService = authorizationService;
        this.notificationService = notificationService;
        this.context = context;
    }

    @GetMapping({"/", "/index"})
    public String getHomePage(Model model) {

        model.addAttribute("template", "content");
        return "index";
    }
}
