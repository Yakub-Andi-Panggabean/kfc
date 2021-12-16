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

import java.util.*;
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
                .map(Role::getMenus)
                .findAny().orElse(Collections.emptySet())
                .stream().filter(m -> m.isEnable()).collect(Collectors.toSet());

        return menus.stream().sorted(Comparator.comparing(Menu::getId)).collect(Collectors.toList());

    }

    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    public Optional<Menu> findMenuById(Long id) {
        return menuRepository.findById(id);
    }


    @Override
    public Optional<Menu> updateMenu(Menu menu) {
        return Optional.of(menuRepository.save(menu));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> addNewRole(Role role) {
        return Optional.of(roleRepository.save(role));
    }

    @Override
    public Optional<Role> findRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Optional<Role> update(Role role) {
        return Optional.of(roleRepository.save(role));
    }
}
