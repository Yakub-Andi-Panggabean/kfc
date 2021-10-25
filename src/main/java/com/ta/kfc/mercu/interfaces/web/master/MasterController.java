package com.ta.kfc.mercu.interfaces.web.master;

import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MasterController {

    public static final String MASTER_PATH = "/master";

    private AuthorizationService authorizationService;

    @Autowired
    public MasterController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping({MASTER_PATH})
    public String getMasterPage(Model model) {

        model.addAttribute("template", "master");

        return "index";
    }

}
