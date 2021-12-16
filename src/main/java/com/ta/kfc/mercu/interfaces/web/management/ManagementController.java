package com.ta.kfc.mercu.interfaces.web.management;

import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

@Controller
public class ManagementController extends ManagementModule {


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
        model.addAttribute("availableMenus", authorizationService.getAllMenus()
                .stream().filter(m -> !m.getPath().equals(MANAGEMENT_MENU_PATH))
                .collect(Collectors.toList()));

        return "index";
    }


}
