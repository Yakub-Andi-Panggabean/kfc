package com.ta.kfc.mercu.infrastructure.db.orm.repository.auth;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);
}
