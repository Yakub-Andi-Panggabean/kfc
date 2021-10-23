package com.ta.kfc.mercu.service.security.bean;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.Role;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.auth.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DefaultUserDetailService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(DefaultUserDetailService.class);

    private UserRepository userRepository;

    @Autowired
    public DefaultUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        UserDetails userDetail = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);

        SecurityContextHolder.getContext().
                setAuthentication(new UsernamePasswordAuthenticationToken(userDetail.getUsername(),
                        userDetail.getPassword(), userDetail.getAuthorities()));

        return userDetail;
    }
}
