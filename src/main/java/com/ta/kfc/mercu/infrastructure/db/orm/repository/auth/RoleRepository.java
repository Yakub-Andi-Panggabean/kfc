package com.ta.kfc.mercu.infrastructure.db.orm.repository.auth;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role,Long> {
}
