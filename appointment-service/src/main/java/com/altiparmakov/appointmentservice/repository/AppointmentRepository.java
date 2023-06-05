package com.altiparmakov.appointmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.altiparmakov.appointmentservice.domain.Appointment;

import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByIdAndPatientId(Long id, Long patientId);

    Optional<Appointment> findByIdAndDoctorId(Long id, Long doctorId);

    Optional<Appointment> findByDoctorIdAndPatientId(Long doctorId, Long patientId);
}
