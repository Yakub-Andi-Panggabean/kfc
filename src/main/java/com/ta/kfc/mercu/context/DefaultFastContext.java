package com.ta.kfc.mercu.context;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.service.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultFastContext implements FastContext {

    private Map<String, Object> map;

    public DefaultFastContext() {
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<User> getUser() {
        return Optional.of((User) map.get(USER_KEY));
    }

    @Override
    public Optional<Object> get(String key) {
        return Optional.of(map.get(key));
    }

    @Override
    public void set(String key, Object value) {
        map.put(key, value);
    }
}
