package com.altiparmakov.surveyservice.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Utils {

    private Utils() {
    }

    private static final DateFormat TIME_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Timestamp dateTimeToTimestamp(String dateTime) {
        try {
            return new Timestamp(TIME_DATE_FORMAT.parse(dateTime).getTime());
        } catch (ParseException e) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST);
        }
    }
}
