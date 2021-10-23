package com.ta.kfc.mercu.interfaces.web.home;

import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private AuthorizationService authorizationService;

    @Autowired
    public HomeController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping({"/", "/index"})
    public String welcome(Model model) {

        model.addAttribute("menus", authorizationService.getAuthorizedMenu());

        return "index";
    }
}
