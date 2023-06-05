package com.altiparmakov.apigateway.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.altiparmakov.apigateway.domain.User;
import com.altiparmakov.apigateway.web.exception.AuthException;
import com.altiparmakov.apigateway.web.exception.ErrorCode;
import com.altiparmakov.apigateway.repository.UserRepository;
import com.altiparmakov.apigateway.security.JwtTokenManager;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenManager tokenManager;

    public String auth(String username, String password) {
        User user = null;

        if (!StringUtils.isEmpty(username)) {
            user = userRepository.findByUsername(username).orElseThrow(
                    () -> new AuthException(HttpStatus.UNAUTHORIZED, ErrorCode.WRONG_LOGIN_CREDENTIALS));
        }

        if (user == null) {
            throw new RuntimeException();
        }

        boolean isMatch = passwordEncoder.matches(password, user.getPassword());

        String jwtToken = null;
        if (isMatch) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole());
            claims.put("userId", user.getId());
            jwtToken = tokenManager.generateToken(username, claims);
        }

        return jwtToken;
    }
}
