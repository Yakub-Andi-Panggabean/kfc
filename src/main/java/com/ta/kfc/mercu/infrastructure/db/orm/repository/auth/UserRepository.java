package com.ta.kfc.mercu.infrastructure.db.orm.repository.auth;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    List<User> findAll();
}
