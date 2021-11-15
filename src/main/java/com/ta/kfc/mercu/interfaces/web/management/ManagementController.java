package com.ta.kfc.mercu.interfaces.web.management;

import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagementController {

    public static final String MANAGEMENT_PATH = "/management";
    public static final String MANAGEMENT_USER_PATH = MANAGEMENT_PATH + "/user";
    public static final String MANAGEMENT_ROLE_PATH = MANAGEMENT_PATH + "/role";
    public static final String MANAGEMENT_SUPPORT_PATH = MANAGEMENT_PATH + "/support";
    public static final String MANAGEMENT_MENU_PATH = MANAGEMENT_PATH + "/menu";


    private AuthorizationService authorizationService;

    @Autowired
    public ManagementController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping({MANAGEMENT_PATH})
    public String getManagementPage(Model model) {

        model.addAttribute("template", "management");
        model.addAttribute("management_template", "user_management");
        return "index";
    }


    @GetMapping({MANAGEMENT_USER_PATH})
    public String getManagementUserPage(Model model) {

        model.addAttribute("template", "management");
        model.addAttribute("management_template", "user_management");
        return "index";
    }


    @GetMapping({MANAGEMENT_ROLE_PATH})
    public String getManagementRolePage(Model model) {

        model.addAttribute("template", "management");
        model.addAttribute("management_template", "role_management");
        return "index";
    }

    @GetMapping({MANAGEMENT_SUPPORT_PATH})
    public String getManagementSupportPage(Model model) {

        model.addAttribute("template", "management");
        model.addAttribute("management_template", "support_management");
        return "index";
    }

    @GetMapping({MANAGEMENT_MENU_PATH})
    public String getManagementMenuPage(Model model) {

        model.addAttribute("template", "management");
        model.addAttribute("management_template", "menu_management");
        return "index";
    }


}
