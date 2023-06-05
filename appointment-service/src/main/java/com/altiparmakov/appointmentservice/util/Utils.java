package com.altiparmakov.appointmentservice.util;

import org.springframework.http.HttpStatus;
import com.altiparmakov.appointmentservice.web.dto.TimeslotDto;
import com.altiparmakov.appointmentservice.web.exception.AppointmentException;
import com.altiparmakov.appointmentservice.web.exception.ErrorCode;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private Utils() {
    }

    private static final DateFormat TIME_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static Timestamp dateTimeToTimestamp(String dateTime) {
        try {
            return new Timestamp(TIME_DATE_FORMAT.parse(dateTime).getTime());
        } catch (ParseException e) {
            throw new AppointmentException(HttpStatus.BAD_REQUEST, ErrorCode.ERROR_PARSING_DATETIME);
        }
    }

    public static TimeslotDto timestampToTimeslotResource(Timestamp timestamp) {
        DateFormat date = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat time = new SimpleDateFormat("HH:mm");
        Date dateTime = new Date(timestamp.getTime());

        return new TimeslotDto(date.format(dateTime), time.format(dateTime));
    }
}
