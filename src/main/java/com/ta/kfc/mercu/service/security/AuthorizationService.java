package com.ta.kfc.mercu.service.security;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.Role;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.security.Menu;

import java.util.List;
import java.util.Optional;

public interface AuthorizationService {
    List<Menu> getAuthorizedMenu();

    List<Menu> getAllMenus();

    Optional<Menu> findMenuById(Long id);

    Optional<Menu> updateMenu(Menu menu);

    List<Role> getAllRoles();

    Optional<Role> addNewRole(Role role);

    Optional<Role> findRoleById(Long id);

    Optional<Role> update(Role role);

}
