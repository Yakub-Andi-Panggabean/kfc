package com.ta.kfc.mercu.infrastructure.db.orm.model.auth;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class RoleMenuId implements Serializable {

    private Long roleId;
    private Long menuId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
