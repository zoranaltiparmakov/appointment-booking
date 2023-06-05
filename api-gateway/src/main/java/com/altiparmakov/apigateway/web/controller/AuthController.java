package com.altiparmakov.apigateway.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.altiparmakov.apigateway.web.dto.TokenDto;
import com.altiparmakov.apigateway.web.dto.UserCredentialsDto;
import com.altiparmakov.apigateway.service.UserService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<TokenDto> auth(@RequestBody UserCredentialsDto user) {
        String token = userService.auth(user.getUsername(), user.getPassword());

        return ResponseEntity.ok(new TokenDto(token));
    }
}
