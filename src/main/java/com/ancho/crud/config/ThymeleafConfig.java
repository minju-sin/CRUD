package com.ancho.crud.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

@Configuration
public class ThymeleafConfig {

        @Bean
        public SpringResourceTemplateResolver thymeleafTemplateResolver(
                SpringResourceTemplateResolver defaultTemplateResolver,
                Thymeleaf3Properties thymeleaf3Properties
        ) {
            defaultTemplateResolver.setUseDecoupledLogic(thymeleaf3Properties.isDecoupledLogic());

            return defaultTemplateResolver;
        }

        @Getter
        @RequiredArgsConstructor
        // @ConstructorBinding 3.0 이상에서는 생성자가 하나일 때 사용할 필요 없음
        @ConfigurationProperties("spring.thymeleaf3")
        public static class Thymeleaf3Properties {
            /**
             * Use Thymeleaf 3 Decoupled Logic
             */
            private final boolean decoupledLogic;
        }
}
