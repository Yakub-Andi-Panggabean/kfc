package com.ta.kfc.mercu.service.security;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;

import java.util.List;
import java.util.Optional;

public interface AuthenticationService {

    boolean auth(String username, String password);

    boolean isAuthenticated();

    Optional<User> findUserByUserName(String username);


}
