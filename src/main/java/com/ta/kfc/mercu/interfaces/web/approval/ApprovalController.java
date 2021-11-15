package com.ta.kfc.mercu.interfaces.web.approval;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApprovalController {

    private static final String APPROVAL_PATH="/approval";

    @GetMapping({APPROVAL_PATH})
    public String getApprovalPage(Model model) {

        model.addAttribute("template", "approval");
        return "index";
    }

}
