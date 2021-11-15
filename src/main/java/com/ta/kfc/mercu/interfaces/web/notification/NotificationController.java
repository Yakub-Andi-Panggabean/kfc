package com.ta.kfc.mercu.interfaces.web.notification;

import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationController {

    public static final String NOTIFICATION_PATH = "/notification";

    private AuthorizationService authorizationService;

    @Autowired
    public NotificationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping({NOTIFICATION_PATH})
    public String getOrderPurchasePage(Model model) {

        model.addAttribute("template", "notification");
        return "index";
    }
}
