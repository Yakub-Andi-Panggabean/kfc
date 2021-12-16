package com.ta.kfc.mercu.interfaces.web.management;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.Role;
import com.ta.kfc.mercu.infrastructure.db.orm.model.security.Menu;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class ManagementProcessorController extends ManagementModule {

    private AuthorizationService authorizationService;

    @Autowired
    public ManagementProcessorController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
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

}
