package com.ta.kfc.mercu.interfaces.web.tagging;

import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TaggingPrintController {

    public static final String TAGGING_PATH = "/tagging";
    public static final String TAGGING_PRINT_PATH = TAGGING_PATH + "/print";

    private AuthorizationService authorizationService;

    @Autowired
    public TaggingPrintController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping({TAGGING_PRINT_PATH})
    public String getPrintTaggingPage(Model model) {

        model.addAttribute("template", "tagging_print");
        return "index";
    }

}
