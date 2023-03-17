package com.barclays.practice.springmvc.congif;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security.authorizeHttpRequests()
                .anyRequest().authenticated()
                .and().httpBasic(Customizer.withDefaults())
                .csrf()
                .ignoringRequestMatchers("/api/**");
        return security.build();
    }
}