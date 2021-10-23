package com.ta.kfc.mercu.interfaces.web.auth;

import com.ta.kfc.mercu.service.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String getLoginPage(HttpServletRequest request, Model model) {

        if (authService.isAuthenticated()) {
            return "redirect:/";
        }

        HttpSession session = request.getSession(false);

        String errorMessage = null;

        if (session != null) {
            Exception ex = (Exception) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                log.error("error occurred", ex);
                errorMessage = ex.getMessage();
            }
        }

        model.addAttribute("error", errorMessage);

        return "login";
    }
    
}
