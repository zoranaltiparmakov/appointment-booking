package com.altiparmakov.appointmentservice.web.dto;

import lombok.Data;

@Data
public class AppointmentDto {

    private Long doctorId;
    private Long medicalServiceId;
    private TimeslotDto timeslot;
}
