package com.ossant.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authConfig -> {
                    authConfig.anyRequest().authenticated();
                })
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}