package com.altiparmakov.appointmentservice.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.altiparmakov.appointmentservice.domain.Appointment;
import com.altiparmakov.appointmentservice.domain.AppointmentStatus;
import com.altiparmakov.appointmentservice.resource.AppointmentResource;
import com.altiparmakov.appointmentservice.service.AppointmentService;
import com.altiparmakov.appointmentservice.web.dto.AppointmentDto;
import com.altiparmakov.appointmentservice.web.dto.PatchAppointmentStatusDto;

import java.sql.Timestamp;

@RestController
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping(value = "/appointments")
    public AppointmentResource getAppointment(@RequestParam(name = "doctorId") Long doctorId,
                                              @RequestParam(name = "patientId") Long patientId,
                                              @RequestParam(name = "dateTime") Timestamp dateTime) {
        return appointmentService.getAppointment(doctorId, patientId, dateTime);
    }

    @PostMapping(value = "/appointments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Appointment> reserveAppointment(@RequestHeader("userId") String patientId,
                                                          @RequestBody AppointmentDto appointmentDto) {
        Appointment appointment = appointmentService.reserveAppointment(Long.parseLong(patientId),
                                                                        appointmentDto.getDoctorId(),
                                                                        appointmentDto.getMedicalServiceId(),
                                                                        appointmentDto.getTimeslot().getFormattedTimeslot());

        return ResponseEntity.ok(appointment);
    }

    @DeleteMapping(value = "/appointments/{id}/cancel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> cancelAppointment(@RequestHeader("userId") String patientId,
                                                  @PathVariable(value = "id") Long appointmentId) {
        appointmentService.cancelAppointment(Long.valueOf(patientId), appointmentId);

        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/appointments/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeAppointmentStatus(@RequestHeader("userId") Long doctorId,
                                                        @PathVariable(value = "id") Long appointmentId,
                                                        @RequestBody PatchAppointmentStatusDto patch) {
        if (AppointmentStatus.valueOf(patch.getAppointmentStatus()).equals(AppointmentStatus.DONE)) {
            appointmentService.finishAppointment(doctorId, appointmentId);
        }

        return ResponseEntity.ok().build();
    }
}
