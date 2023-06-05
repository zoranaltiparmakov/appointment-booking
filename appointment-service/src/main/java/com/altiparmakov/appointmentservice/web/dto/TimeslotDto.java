package com.altiparmakov.appointmentservice.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class TimeslotDto {

    private Long id;
    private String date;
    private String time;

    public TimeslotDto(String date, String time) {
        this.date = date;
        this.time = time;
    }

    @JsonIgnore
    public String getFormattedTimeslot() {
        return String.format("%s %s", date, time);
    }
}
