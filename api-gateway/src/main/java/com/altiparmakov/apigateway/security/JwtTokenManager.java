package com.altiparmakov.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.altiparmakov.apigateway.web.exception.AuthException;
import com.altiparmakov.apigateway.web.exception.ErrorCode;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenManager {

    private static final int TOKEN_VALIDITY = 3600;
    @Value("${jwt.secret}")
    private String jwtSecret;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public Claims getAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, ErrorCode.TOKEN_EXPIRED);
        } catch (Exception ex) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, ErrorCode.TOKEN_INVALID);
        }
    }

    public boolean isTokenValid(String token) {
        return !this.getAllClaims(token).getExpiration().before(new Date());
    }

    public String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(key)
                .compact();
    }
}
