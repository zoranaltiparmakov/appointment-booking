package com.altiparmakov.appointmentservice.web.dto;

import lombok.Data;

@Data
public class FinishAppointmentDto {

    private Long patientId;
    private Long medicalServiceId;
    private TimeslotDto timeslot;
}
