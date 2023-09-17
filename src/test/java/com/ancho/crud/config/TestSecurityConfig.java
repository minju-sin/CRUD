package com.ancho.crud.config;

import com.ancho.crud.domain.UserAccount;
import com.ancho.crud.repository.UserAccountRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private UserAccountRepository userAccountRepository;

    @BeforeTestMethod   //  실행 테스트를 실행하기 전에 테스트 메소드 실행해서 테스트 값을 넣어줌
    public void securitySetUp(){
        given(userAccountRepository.findById(anyString())).willReturn(Optional.of(UserAccount.of(
                "anchoTest",
                "pw",
                "ancho-test@email.com",
                "ancho-test",
                "test memo"
        )));
    }


}
