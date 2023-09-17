package com.ancho.crud.config;

import com.ancho.crud.dto.UserAccountDto;
import com.ancho.crud.dto.security.BoardPrincipal;
import com.ancho.crud.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) ->authz
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()   //  39 줄과 의미 동일함
                        .requestMatchers(
                            HttpMethod.GET,
                                "/",
                                "/articles",
                                "/articles/search-hashtag"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin()
                    .and()  //  로그인
                .logout()   //  로그아웃 기능에서 로그아웃을 성공하면 루트 페이지로 이동하도록 설정함
                    .logoutSuccessUrl("/")
                    .and();
        //  기존에 antMatchers나 mvcMatchers를 사용했다면 requestMatchers로 변경해야 함.
        //  Security 버전이 스프링 3.0 이상에서는 변경된 점이 많아서 공식 사이트를 보며 해결함...
        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        // static resource, css - js 가 있을 수 있다. 이런 것들은 권한 체크가 필요 없다..
//        // 내가 직접 지정하지 않아도 정적 리스트를 인증 부분에서 제외시킬 수 있음
//        //  이제 이 방법을 사용하지 않고 위에서 추가하는 방법을 사용하면 됨
//        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository){
        // DB에 저장된 값을 불러옴

        return username -> userAccountRepository
                .findById(username)
                .map(UserAccountDto::from)
                .map(BoardPrincipal::from)
                .orElseThrow(()->new UsernameNotFoundException(("유저를 찾을 수 없습니다 - username: " + username)));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //  암호화
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
