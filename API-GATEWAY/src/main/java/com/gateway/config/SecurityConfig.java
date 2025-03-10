package com.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http)throws Exception{
        return http
                .csrf(c->c.disable())
                .cors(cor->cor.disable())
                .authorizeExchange(req->{
                    req.pathMatchers("/auth/**").permitAll();
                    req.pathMatchers("/notification/**").hasAnyRole("USER","ADMIN");
                    req.pathMatchers("/booking/api/v1/booking/property/**").hasRole("OWNER");
                    req.anyExchange().authenticated();
                })
                .addFilterBefore(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
