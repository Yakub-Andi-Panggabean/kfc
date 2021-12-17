package com.ta.kfc.mercu.service.security;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.security.Menu;

import java.util.List;
import java.util.Optional;

public interface AuthenticationService {

    boolean auth(String username, String password);

    boolean isAuthenticated();

    Optional<User> findUserByUserName(String username);

    Optional<User> addNewUser(User user);

    Optional<User> updateUser(User user);

    List<User> findAll();


}
