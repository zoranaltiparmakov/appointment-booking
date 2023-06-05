package com.altiparmakov.surveyservice.client.resource;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AppointmentResource {

    private Long doctorId;
    private Long patientId;
    private Timestamp dateTime;
    private AppointmentStatus status;
}
