package com.ta.kfc.mercu.service.security.bean;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.Role;
import com.ta.kfc.mercu.infrastructure.db.orm.model.security.Menu;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.auth.RoleRepository;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.security.MenuRepository;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DefaultAuthorizationService implements AuthorizationService {

    private MenuRepository menuRepository;
    private RoleRepository roleRepository;

    @Autowired
    public DefaultAuthorizationService(MenuRepository menuRepository, RoleRepository roleRepository) {
        this.menuRepository = menuRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Menu> getAuthorizedMenu() {

        List<SimpleGrantedAuthority> authorities =
                (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext()
                        .getAuthentication().getAuthorities();

        Set<Menu> menus = authorities.stream().map(SimpleGrantedAuthority::getAuthority)
                .map(roleRepository::findByName).collect(Collectors.toList())
                .stream()
                .map(Role::getMenus).findAny().orElse(Collections.emptySet());

        return menus.stream().sorted(Comparator.comparing(Menu::getId)).collect(Collectors.toList());

    }
}
