package com.altiparmakov.apigateway.filter;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import com.altiparmakov.apigateway.web.exception.AuthException;
import com.altiparmakov.apigateway.web.exception.ErrorCode;
import com.altiparmakov.apigateway.security.JwtTokenManager;

import static com.altiparmakov.apigateway.filter.AuthFilter.getAuthHeader;

@Configuration
@RequiredArgsConstructor
public class DoctorAuthFilter implements GatewayFilter {

    private final JwtTokenManager jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        final String token = getAuthHeader(request);
        Claims claims = jwtUtil.getAllClaims(token);

        if (!claims.get("role").equals("DOCTOR")) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, ErrorCode.OPERATION_NOT_ALLOWED);
        }

        return chain.filter(exchange);
    }
}
