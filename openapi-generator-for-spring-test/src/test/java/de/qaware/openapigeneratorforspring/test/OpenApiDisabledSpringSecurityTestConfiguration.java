package de.qaware.openapigeneratorforspring.test;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Disables Spring Security by default, which is activated because some
 * tests focus on CSRF support and thus Spring Security Basic Auth kicks in.
 */
public class OpenApiDisabledSpringSecurityTestConfiguration {
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter() {
        return new WebSecurityConfigurerAdapter() {
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                // do nothing, this disables all security for tests by default
            }
        };
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnMissingBean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        // do nothing, this disables all security for tests by default
        return http.build();
    }
}
