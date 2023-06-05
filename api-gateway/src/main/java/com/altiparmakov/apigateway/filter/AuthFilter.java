package com.altiparmakov.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import com.altiparmakov.apigateway.config.RouterValidator;
import com.altiparmakov.apigateway.security.JwtTokenManager;

import static org.apache.commons.lang.StringUtils.isEmpty;

@Configuration
@RequiredArgsConstructor
public class AuthFilter implements GatewayFilter {

    private final JwtTokenManager tokenManager;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (RouterValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request)) {
                throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "Authorization header is missing.");
            }

            final String token = getAuthHeader(request);

            try {
                if (!tokenManager.isTokenValid(token)) {
                    throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "Token is invalid.");
                }
            } catch (MalformedJwtException ex) {
                throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "Invalid JWT token.");
            }

            this.populateRequestWithHeaders(exchange, token);
        }

        return chain.filter(exchange);
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = tokenManager.getAllClaims(token);
        exchange.getRequest()
                .mutate()
                .header("userId", String.valueOf(claims.get("userId")))
                .header("role", String.valueOf(claims.get("role")))
                .build();
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }

    protected static String getAuthHeader(ServerHttpRequest request) {
        String token = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);

        if (isEmpty(token) || !token.contains("Bearer")) {
            throw new RuntimeException();
        }

        return token.replace("Bearer", "").strip();
    }
}
