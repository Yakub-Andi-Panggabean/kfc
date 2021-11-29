package com.ta.kfc.mercu.context;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;

import java.util.Optional;

public interface FastContext {

    String USER_KEY = "user";

    Optional<User> getUser();

    Optional<Object> get(String key);

    void set(String key, Object value);
}
