package com.ta.kfc.mercu.infrastructure.db.orm.model.auth;

import com.ta.kfc.mercu.infrastructure.db.orm.model.security.Menu;

import javax.persistence.*;

@Entity
@Table(name = "role_menus")
public class RoleMenu {

    @EmbeddedId
    private RoleMenuId roleMenuId = new RoleMenuId();

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "roles_id")
    private Role role;
    @ManyToOne
    @MapsId("menuId")
    @JoinColumn(name = "menus_id")
    private Menu menu;

    private boolean allowView;
    private boolean allowCreate;
    private boolean allowUpdate;
    private boolean allowDelete;

    public RoleMenuId getRoleMenuId() {
        return roleMenuId;
    }

    public void setRoleMenuId(RoleMenuId roleMenuId) {
        this.roleMenuId = roleMenuId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public boolean isAllowView() {
        return allowView;
    }

    public void setAllowView(boolean allowView) {
        this.allowView = allowView;
    }

    public boolean isAllowCreate() {
        return allowCreate;
    }

    public void setAllowCreate(boolean allowCreate) {
        this.allowCreate = allowCreate;
    }

    public boolean isAllowUpdate() {
        return allowUpdate;
    }

    public void setAllowUpdate(boolean allowUpdate) {
        this.allowUpdate = allowUpdate;
    }

    public boolean isAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(boolean allowDelete) {
        this.allowDelete = allowDelete;
    }
}