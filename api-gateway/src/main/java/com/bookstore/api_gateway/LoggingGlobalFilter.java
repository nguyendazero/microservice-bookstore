package com.bookstore.api_gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class LoggingGlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingGlobalFilter.class);

    @Bean
    public GlobalFilter logRequestAndResponseFilter() {
        return (exchange, chain) -> {
            // Ghi log thông tin về request
            logger.info("Request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());

            // Ghi log thông tin về response sau khi xử lý request
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("Response Status Code: {}", exchange.getResponse().getStatusCode());
            }));
        };
    }
}
