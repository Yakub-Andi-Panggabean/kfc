package com.ta.kfc.mercu.config;

import com.ta.kfc.mercu.context.DefaultFastContext;
import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.service.security.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class AppConfig {

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
    public FastContext getContext() {
        return new DefaultFastContext();
    }

}
