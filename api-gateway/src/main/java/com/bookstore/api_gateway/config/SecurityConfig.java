package com.bookstore.api_gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/public/**").permitAll()
                        .pathMatchers("/api/account/admin/**").hasAuthority("ROLE_ADMIN")
                        .pathMatchers("/api/account/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        .anyExchange().authenticated()
                )
                .addFilterAt(new JwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

    private class JwtAuthenticationFilter implements WebFilter {

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            // Log token nhận được
            System.out.println("Received Token: " + token);

            // Nếu không có token hoặc token không phải là Bearer token, tiếp tục chain
            if (token == null || !token.startsWith("Bearer ")) {
                return chain.filter(exchange);
            }

            token = token.substring(7);  // Lấy token bỏ đi "Bearer "

            try {
                // Log token đã được trích xuất
                System.out.println("Extracted Token: " + token);

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtSecret)  // Đảm bảo sử dụng secretKey giống nhau
                        .build()
                        .parseClaimsJws(token)
                        .getBody();


                // Log claims nhận được
                System.out.println("Claims from Token: " + claims);

                String username = claims.getSubject();
                List<String> roles = claims.get("roles", List.class);

                // Log roles
                System.out.println("Roles from token: " + roles);

                if (roles == null || roles.isEmpty()) {
                    return chain.filter(exchange);  // Hoặc trả về lỗi 401 nếu cần thiết
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                );

                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authToken));

            } catch (Exception e) {
                // Log lỗi nếu có
                System.out.println("Error decoding token: " + e.getMessage());
                return exchange.getResponse().setComplete();
            }
        }
    }

}
