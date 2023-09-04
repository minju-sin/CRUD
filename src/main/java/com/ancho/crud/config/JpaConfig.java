package com.ancho.crud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

//    이 기능을 사용하면 게시글을 작성했을 때, 작성자의 이름을 자동으로 생성해 준다.
    @Bean
    public AuditorAware<String> auditorAware(){
        return () -> Optional.of("minju");  //  TODO : 스프링 시큐리티로 인증 기능을 붙이게 될 때, 수정하자
    }
}
