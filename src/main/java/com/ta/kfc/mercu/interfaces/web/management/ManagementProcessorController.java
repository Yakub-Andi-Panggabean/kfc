package com.ta.kfc.mercu.interfaces.web.management;

import com.ta.kfc.mercu.dto.management.UserRegistration;
import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.Role;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.security.Menu;
import com.ta.kfc.mercu.service.security.AuthenticationService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class ManagementProcessorController extends ManagementModule {

    private AuthorizationService authorizationService;
    private AuthenticationService authenticationService;

    @Autowired
    public ManagementProcessorController(AuthorizationService authorizationService,
                                         AuthenticationService authenticationService) {
        this.authorizationService = authorizationService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(MANAGEMENT_MENU_PATH + "/{action}/{menuid}")
    public String disableMenu(@PathVariable("action") String action, @PathVariable("menuid") Long id) {

        Optional<Menu> menu = authorizationService.findMenuById(id);

        if (menu.isPresent()) {

            switch (action) {
                case "status":
                    menu.get().setEnable(!menu.get().isEnable());
                    break;
            }

            authorizationService.updateMenu(menu.get());

        }

        return String.format("redirect:%s", MANAGEMENT_MENU_PATH);
    }

    @PostMapping(MANAGEMENT_ROLE_PATH)
    public String addNewRole(Role role) {

        role.setName(String.format("ROLE_%s", role.getName().toUpperCase()));
        authorizationService.addNewRole(role);
        return String.format("redirect:%s", MANAGEMENT_ROLE_PATH);
    }


    @PostMapping("/role/menu/{action}/{roleId}/{menuId}")
    public String addMenuToRole(@PathVariable("action") String action,
                                @PathVariable("roleId") Long roleId,
                                @PathVariable("menuId") Long menuId) {

        Optional<Role> role = authorizationService.findRoleById(roleId);

        if (role.isPresent()) {

            Optional<Menu> menu = authorizationService.findMenuById(menuId);

            if (menu.isPresent()) {

                switch (action) {
                    case "add":
                        role.get().getMenus().add(menu.get());
                        break;
                    case "remove":
                        role.get().getMenus().remove(menu.get());
                        break;
                }


                authorizationService.update(role.get());
            }
        }

        return String.format("redirect:%s?isUpdate=true&roleId=%d", MANAGEMENT_ROLE_PATH, roleId);
    }


    @PostMapping(MANAGEMENT_USER_PATH)
    public String registerUser(UserRegistration req) {

        UserDetail detail = new UserDetail();
        detail.setBirthDate(new Date());
        detail.setCreatedDate(new Date());
        detail.setCode(req.getEmployeeId());
        detail.setDepartment(req.getDepartment());
        detail.setFirstName(req.getFirstName());
        detail.setLastName(req.getLastName());
        detail.setUnit(req.getUnit());
        detail.setPosition(req.getPosition());

        Set<Role> roles = new HashSet<>();
        roles.add(req.getRole());

        User user = new User();
        user.setUserDetail(detail);
        user.setRoles(roles);
        user.setUsername(req.getEmployeeId());
        user.setPassword(req.getEmployeeId());

        authenticationService.addNewUser(user);
        return String.format("redirect:%s", MANAGEMENT_USER_PATH);
    }

}
