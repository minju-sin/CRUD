package com.ancho.crud.config;

import com.ancho.crud.dto.security.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

//    이 기능을 사용하면 게시글을 작성했을 때, 작성자의 이름을 자동으로 생성해 준다.
    @Bean
    public AuditorAware<String> auditorAware(){
        //  ofNullable 을 사용하는 이유는 인증이 안된 경우에도 사용할 수 있어야 하기 때문이다.
        //  SecurityContextHolder 라는 인증 정보를 모두 가지고 있는 클래스 임
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)    //  로그인 상태 유무 확인
                .map(Authentication::getPrincipal)  //  보편적인 Principal 정보
                .map(BoardPrincipal.class::cast)    //  내가 작성한 Principal의 클래스 정보 얻어옴
                .map(BoardPrincipal::getUsername);  //  그 중에서 유저 정보를 불러옴
    }
}
