package com.ta.kfc.mercu.infrastructure.db.orm.model.security;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.Role;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String label;
    private String path;

    @ManyToMany(mappedBy = "menus")
    private Set<Role> roles;

    @ManyToMany
    private List<Menu> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }
}
