package com.ta.kfc.mercu.interfaces.web.management;

import com.ta.kfc.mercu.dto.management.UserRegistration;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.Role;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Department;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;
import com.ta.kfc.mercu.infrastructure.db.orm.model.security.Menu;
import com.ta.kfc.mercu.service.master.MasterService;
import com.ta.kfc.mercu.service.security.AuthenticationService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ManagementPageController extends ManagementModule {


    private AuthorizationService authorizationService;
    private MasterService masterService;
    private AuthenticationService authenticationService;

    @Autowired
    public ManagementPageController(AuthorizationService authorizationService,
                                    MasterService masterService,
                                    AuthenticationService authenticationService) {
        this.authorizationService = authorizationService;
        this.masterService = masterService;
        this.authenticationService = authenticationService;
    }

    @GetMapping({MANAGEMENT_PATH})
    public String getManagementPage(Model model) {

        return getManagementUserPage(model);
    }


    @GetMapping({MANAGEMENT_USER_PATH})
    public String getManagementUserPage(Model model) {

        model.addAttribute("template", "management");
        model.addAttribute("management_template", "user_management");
        model.addAttribute("availableRoles", authorizationService.getAllRoles().stream()
                .filter(r -> !r.getName().equals("ROLE_ROOT"))
                .collect(Collectors.toList()));
        model.addAttribute("availableDepartments", masterService.getAllDepartments()
                .stream()
                .filter(Department::isEnable)
                .collect(Collectors.toList()));
        model.addAttribute("availableUnits", masterService.getAllUnit()
                .stream()
                .filter(Unit::isEnable)
                .collect(Collectors.toList()));
        model.addAttribute("userModel", new UserRegistration());
        model.addAttribute("users", authenticationService.findAll());

        return "index";
    }


    @GetMapping({MANAGEMENT_ROLE_PATH})
    public String getManagementRolePage(@RequestParam(name = "isUpdate", required = false) boolean isUpdate,
                                        @RequestParam(name = "roleId", required = false) Long roleId, Model model) {

        model.addAttribute("template", "management");
        model.addAttribute("management_template", "role_management");
        model.addAttribute("availableMenus", authorizationService.getAllMenus()
                .stream().filter(m -> m.isEnable())
                .collect(Collectors.toList()));
        model.addAttribute("availableRoles", authorizationService.getAllRoles());
        model.addAttribute("roleModel", new Role());
        model.addAttribute("isUpdate", isUpdate);
        if (roleId != null) {
            model.addAttribute("selectedRoleId", roleId);
            Optional<Role> role = authorizationService.findRoleById(roleId);
            if (role.isPresent()) {
                model.addAttribute("selectedRole", role.get());
                model.addAttribute("availableMenus", authorizationService.getAllMenus()
                        .stream().filter(m -> !role.get().getMenus().contains(m))
                        .collect(Collectors.toList()));
            }
        }

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
