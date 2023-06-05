package com.altiparmakov.appointmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.altiparmakov.appointmentservice.domain.Timetable;

import java.util.Optional;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    Optional<Timetable> findByDoctorId(Long doctorId);
}
