package com.altiparmakov.appointmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.altiparmakov.appointmentservice.domain.Timeslot;

import java.sql.Timestamp;
import java.util.List;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {
    List<Timeslot> findAllByDateTimeAndIsAvailableIsTrue(Timestamp time);
}
