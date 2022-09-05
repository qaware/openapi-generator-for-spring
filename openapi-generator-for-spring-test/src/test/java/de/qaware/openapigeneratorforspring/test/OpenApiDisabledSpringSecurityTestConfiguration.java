package de.qaware.openapigeneratorforspring.test;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Disables Spring Security by default, which is activated because some
 * tests focus on CSRF support and thus Spring Security Basic Auth kicks in.
 */
public class OpenApiDisabledSpringSecurityTestConfiguration {
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnMissingBean
    @Bean
    public SecurityFilterChain noWebMvcSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.build();
    }

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnMissingBean
    @Bean
    public SecurityWebFilterChain noWebFluxSecurityFilterChain(ServerHttpSecurity http) {
        // do nothing, this disables all security for tests by default
        return http.build();
    }
}
