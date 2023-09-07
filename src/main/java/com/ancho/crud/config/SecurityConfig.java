package com.ancho.crud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) ->authz.anyRequest().permitAll())
                .formLogin().and();
        //  Security 버전이 스프링 3.0 이상에서는 변경된 점이 많아서 공식 사이트를 보며 해결함...
        return http.build();
    }
}
