package com.altiparmakov.appointmentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.altiparmakov.appointmentservice.client.user.DoctorResource;
import com.altiparmakov.appointmentservice.client.user.UserClient;
import com.altiparmakov.appointmentservice.domain.Timeslot;
import com.altiparmakov.appointmentservice.domain.Timetable;
import com.altiparmakov.appointmentservice.repository.TimeslotRepository;
import com.altiparmakov.appointmentservice.repository.TimetableRepository;
import com.altiparmakov.appointmentservice.util.Utils;
import com.altiparmakov.appointmentservice.web.dto.FreeTimeslotDto;
import com.altiparmakov.appointmentservice.web.dto.TimeslotDto;
import com.altiparmakov.appointmentservice.web.exception.AppointmentException;
import com.altiparmakov.appointmentservice.web.exception.ErrorCode;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimeslotService {

    private final TimeslotRepository timeslotRepository;

    private final TimetableRepository timetableRepository;

    private final UserClient userClient;

    @Transactional
    public Timetable createTimetable(List<TimeslotDto> timeslots, Long doctorId) {
        List<Timeslot> timeslotsEntities = timeslots.stream()
                .map(this::createTimeslotEntity)
                .toList();
        timeslotRepository.saveAll(timeslotsEntities);
        Timetable timetable = timetableRepository.findByDoctorId(doctorId).orElse(new Timetable(doctorId));
        timetable.getTimeslots().addAll(timeslotsEntities);
        return timetableRepository.save(timetable);
    }

    private Timeslot createTimeslotEntity(TimeslotDto ts) {
        Timestamp timestamp = Utils.dateTimeToTimestamp(ts.getFormattedTimeslot());
        return new Timeslot(timestamp);
    }

    public List<FreeTimeslotDto> getFreeTimeslots(String medicalService) {
        List<DoctorResource> doctors = userClient.getDoctorsByService(medicalService);
        List<FreeTimeslotDto> timeslotsResources = new ArrayList<>();

        doctors.forEach(doctor -> {
            Optional<Timetable> tt = timetableRepository.findByDoctorId(doctor.getId());

            tt.ifPresent(timetable -> {
                List<TimeslotDto> freeTimeslots = timetable.getTimeslots().stream()
                        .filter(Timeslot::getIsAvailable)
                        .map(this::prepareTimeslotDto)
                        .toList();
                FreeTimeslotDto freeTimeslotDto = new FreeTimeslotDto();
                freeTimeslotDto.setDoctorId(doctor.getId());
                freeTimeslotDto.setDoctorFullName(doctor.getFullName());
                freeTimeslotDto.setServiceName(medicalService);
                freeTimeslotDto.setRating(doctor.getRating());
                freeTimeslotDto.setAvailableTimeslots(freeTimeslots);
                timeslotsResources.add(freeTimeslotDto);
            });
        });

        return timeslotsResources;
    }

    private TimeslotDto prepareTimeslotDto(Timeslot timeslot) {
        TimeslotDto timeslotDto = Utils.timestampToTimeslotResource(timeslot.getDateTime());
        timeslotDto.setId(timeslot.getId());
        return timeslotDto;
    }

    public Timeslot getAvailableTimeslotByTime(String dateTime) {
        Timestamp timestamp = Utils.dateTimeToTimestamp(dateTime);
        return timeslotRepository.findAllByDateTimeAndIsAvailableIsTrue(timestamp)
                .stream()
                .findFirst()
                .orElseThrow(() -> new AppointmentException(HttpStatus.NOT_FOUND, ErrorCode.ERROR_PARSING_DATETIME));
    }
}
