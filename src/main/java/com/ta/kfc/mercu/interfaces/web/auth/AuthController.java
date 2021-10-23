package com.ta.kfc.mercu.interfaces.web.auth;

import com.ta.kfc.mercu.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {

        if (authService.isAuthenticated()) {
            return "redirect:/";
        }

        return "login";
    }

//    @PostMapping("/login")
//    public String login(Model model) {
//        return "login";
//    }

}
