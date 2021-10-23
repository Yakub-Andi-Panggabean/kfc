package com.ta.kfc.mercu.service.auth;

public interface AuthService {

    boolean auth(String username, String password);

    boolean isAuthenticated();

}
