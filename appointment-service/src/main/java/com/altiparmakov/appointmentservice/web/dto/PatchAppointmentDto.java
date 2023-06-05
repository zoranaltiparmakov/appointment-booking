package com.altiparmakov.appointmentservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.altiparmakov.appointmentservice.domain.AppointmentStatus;

@Data
@AllArgsConstructor
public class PatchAppointmentDto {

    private AppointmentStatus appointmentStatus;
}
