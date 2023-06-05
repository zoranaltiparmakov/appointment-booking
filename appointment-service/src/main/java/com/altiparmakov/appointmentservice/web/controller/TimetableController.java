package com.altiparmakov.appointmentservice.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.altiparmakov.appointmentservice.domain.Timetable;
import com.altiparmakov.appointmentservice.service.TimeslotService;
import com.altiparmakov.appointmentservice.web.dto.FreeTimeslotDto;
import com.altiparmakov.appointmentservice.web.dto.TimeslotDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TimetableController {

    private final TimeslotService timeslotService;

    @PostMapping(value = "/timetable", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Timetable> createTimetable(@RequestBody List<TimeslotDto> timeslots,
                                                     @RequestHeader(name = "userId") Long doctorId) {
        Timetable timetable = timeslotService.createTimetable(timeslots, doctorId);

        return ResponseEntity.ok(timetable);
    }

    /**
     * Returns free timeslots and doctors for given <strong>medicalService</strong>.
     *
     * @param medicalService
     * @return
     */
    @GetMapping(value = "/timeslots", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FreeTimeslotDto>> getFreeTimeslots(@RequestParam String medicalService) {
        List<FreeTimeslotDto> timeslot = timeslotService.getFreeTimeslots(medicalService);

        return ResponseEntity.ok(timeslot);
    }
}
