package com.altiparmakov.appointmentservice.client.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorResource {
    private Long id;
    private String fullName;
    private Double rating;
}