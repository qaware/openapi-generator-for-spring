package de.qaware.openapigeneratorforspring.test.app39;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
class App39SpringSecurityConfiguration {
    @Bean
    public SecurityFilterChain noWebMvcSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable().build();
    }
}
