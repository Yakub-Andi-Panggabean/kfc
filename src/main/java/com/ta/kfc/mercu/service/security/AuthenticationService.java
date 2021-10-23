package com.ta.kfc.mercu.service.security;

public interface AuthenticationService {

    boolean auth(String username, String password);

    boolean isAuthenticated();

}
