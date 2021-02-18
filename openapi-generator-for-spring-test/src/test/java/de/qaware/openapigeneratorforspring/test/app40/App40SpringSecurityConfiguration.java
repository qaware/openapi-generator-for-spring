package de.qaware.openapigeneratorforspring.test.app40;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.server.csrf.CsrfWebFilter;
import org.springframework.web.server.WebFilter;

@Configuration
public class App40SpringSecurityConfiguration {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public WebFilter skipCsrfFilter() {
        return (exchange, chain) -> {
            CsrfWebFilter.skipExchange(exchange);
            return chain.filter(exchange);
        };
    }
}
