package com.altiparmakov.appointmentservice.web.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    ERROR_PARSING_DATETIME("Date and time seem to be in an invalid format. Make sure to use dd.MM.yyyy HH:mm"),
    INVALID_TIMESLOT("Timeslot does not exist.");

    private final String description;
}
