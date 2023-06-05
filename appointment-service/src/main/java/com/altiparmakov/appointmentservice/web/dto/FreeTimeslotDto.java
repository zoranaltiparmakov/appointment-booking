package com.altiparmakov.appointmentservice.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class FreeTimeslotDto {
    private Long doctorId;
    private String doctorFullName;
    private String serviceName;
    private Double rating;
    private List<TimeslotDto> availableTimeslots;
}
