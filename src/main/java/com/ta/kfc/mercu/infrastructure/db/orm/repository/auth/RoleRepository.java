package com.ta.kfc.mercu.infrastructure.db.orm.repository.auth;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);

    List<Role> findAll();
}
