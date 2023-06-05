package com.altiparmakov.appointmentservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    private Long patientId;
    private Long doctorId;
    private Long ratingId;
    private Long medicalServiceId;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    private Timestamp startDateTimeActual;
    private Timestamp endDateTime;

    @OneToOne
    @JoinColumn(name = "timeslot_id")
    private Timeslot timeslot;
}
