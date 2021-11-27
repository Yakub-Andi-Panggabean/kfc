package com.ta.kfc.mercu.service.security.bean;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.auth.UserRepository;
import com.ta.kfc.mercu.service.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultAuthenticationService implements AuthenticationService {

    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    @Autowired
    public DefaultAuthenticationService(UserDetailsService userDetailsService,
                                        AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @Override
    public boolean auth(String username, String password) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (AuthenticationException aex) {
            return false;
        }
        return true;
    }


    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @Override
    public Optional<User> findUserByUserName(String username) {
        return Optional.of(userRepository.findByUsername(username));
    }
}
