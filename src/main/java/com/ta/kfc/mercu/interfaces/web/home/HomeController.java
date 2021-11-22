package com.ta.kfc.mercu.interfaces.web.page.home;

import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private AuthorizationService authorizationService;

    @Autowired
    public HomeController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping({"/", "/index"})
    public String getHomePage(Model model) {

        model.addAttribute("template", "content");

        return "index";
    }
}
