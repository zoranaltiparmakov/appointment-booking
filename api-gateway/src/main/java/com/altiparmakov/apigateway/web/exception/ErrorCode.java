package com.altiparmakov.apigateway.web.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    MISSING_AUTH_HEADER("Authorization header is missing."),
    TOKEN_EXPIRED("Token expired. Please authenticate again."),
    TOKEN_INVALID("Invalid token. Please verify you have the right token."),
    OPERATION_NOT_ALLOWED("Operation not allowed for the current user."),
    WRONG_LOGIN_CREDENTIALS("Wrong username/password.");

    private final String description;
}
