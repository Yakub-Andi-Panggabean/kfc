package com.ta.kfc.mercu.interfaces.web.auth;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.dto.credential.ChangePasswordModel;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.service.security.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private static final String CREDENTIAL_PATH = "/credential";

    private AuthenticationService authService;
    private FastContext context;

    @Autowired
    public AuthController(AuthenticationService authService, FastContext context) {
        this.authService = authService;
        this.context = context;
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {

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

        if (authService.isAuthenticated()) {
            return "redirect:/";
        }

        return "login";
    }


    @GetMapping({CREDENTIAL_PATH})
    public String getChangePasswordPage(Model model) {
        model.addAttribute("template", "change_password");
        model.addAttribute("changePasswordModel", new ChangePasswordModel());
        return "index";
    }

    @PostMapping({CREDENTIAL_PATH})
    public String changePassword(ChangePasswordModel req) {

        if (!req.isValid()) {
            throw new RuntimeException("password confirmation not match");
        }
        User user = context.getUser().get();
        user.setPassword(req.getNewPassword());
        authService.updateUser(user);

        return String.format("redirect:%s", CREDENTIAL_PATH);
    }


}
