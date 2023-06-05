package com.altiparmakov.apigateway.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AuthException extends ResponseStatusException {
    public AuthException(HttpStatus status) {
        super(status);
    }

    public AuthException(HttpStatus status, ErrorCode reason) {
        super(status, reason.getDescription());
    }

    public AuthException(HttpStatus status, ErrorCode reason, Throwable cause) {
        super(status, reason.getDescription(), cause);
    }

    public AuthException(int rawStatusCode, ErrorCode reason, Throwable cause) {
        super(rawStatusCode, reason.getDescription(), cause);
    }
}
