package com.altiparmakov.appointmentservice.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AppointmentException extends ResponseStatusException {
    public AppointmentException(HttpStatus status) {
        super(status);
    }

    public AppointmentException(HttpStatus status, ErrorCode reason) {
        super(status, reason.getDescription());
    }

    public AppointmentException(HttpStatus status, ErrorCode reason, Throwable cause) {
        super(status, reason.getDescription(), cause);
    }

    public AppointmentException(int rawStatusCode, ErrorCode reason, Throwable cause) {
        super(rawStatusCode, reason.getDescription(), cause);
    }
}
