package com.ta.kfc.mercu.interfaces.web.management;

import com.ta.kfc.mercu.infrastructure.db.orm.model.security.Menu;
import com.ta.kfc.mercu.service.security.AuthenticationService;
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

}
