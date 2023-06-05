package com.altiparmakov.appointmentservice.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.altiparmakov.appointmentservice.domain.AppointmentStatus;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class AppointmentResource {

    private Long doctorId;
    private Long patientId;
    private Timestamp dateTime;
    private AppointmentStatus status;
}
