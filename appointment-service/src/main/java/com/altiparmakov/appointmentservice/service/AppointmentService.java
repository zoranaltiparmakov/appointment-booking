package com.altiparmakov.appointmentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.altiparmakov.appointmentservice.domain.Appointment;
import com.altiparmakov.appointmentservice.domain.AppointmentStatus;
import com.altiparmakov.appointmentservice.domain.Timeslot;
import com.altiparmakov.appointmentservice.repository.AppointmentRepository;
import com.altiparmakov.appointmentservice.resource.AppointmentResource;
import com.altiparmakov.appointmentservice.web.exception.AppointmentException;
import com.altiparmakov.protos.AppointmentEvent;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AppointmentService {

    public static final String SURVEY_TOPIC_NAME = "appointment_event";
    private final AppointmentRepository appointmentRepository;
    private final TimeslotService timeslotService;
    private final MessagingService messagingService;

    public Appointment reserveAppointment(Long patientId, Long doctorId, Long medicalServiceId, String timeslot) {
        Appointment appointment = new Appointment();
        appointment.setDoctorId(doctorId);
        appointment.setPatientId(patientId);
        appointment.setMedicalServiceId(medicalServiceId);
        appointment.setStatus(AppointmentStatus.RESERVED);
        Timeslot timeslotEntity = timeslotService.getAvailableTimeslotByTime(timeslot);
        timeslotEntity.setIsAvailable(false);
        appointment.setTimeslot(timeslotEntity);
        return appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Long patientId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findByIdAndPatientId(appointmentId, patientId).orElseThrow(
                () -> new AppointmentException(HttpStatus.BAD_REQUEST));

        appointment.setStatus(AppointmentStatus.CANCELED);
        appointment.getTimeslot().setIsAvailable(true);
        appointmentRepository.save(appointment);
    }

    public void finishAppointment(Long doctorId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findByIdAndDoctorId(appointmentId, doctorId).orElseThrow(
                () -> new AppointmentException(HttpStatus.BAD_REQUEST));
        appointment.setStatus(AppointmentStatus.DONE);
        appointmentRepository.save(appointment);
        log.info("Appointment {} was done.", appointment.getId());

        AppointmentEvent appointmentEvent = AppointmentEvent.newBuilder().setDoctorId(
                appointment.getDoctorId().toString()).setCaseDateTime(
                appointment.getTimeslot().getDateTime().toString()).setMedicalService(
                appointment.getMedicalServiceId().toString()).build();
        messagingService.sendMessage(SURVEY_TOPIC_NAME, "status.change", appointmentEvent);
    }

    public boolean existAppointment(Long doctorId, Long patientId, Timestamp dateTime) {
        Optional<Appointment> appointment = appointmentRepository.findByDoctorIdAndPatientId(doctorId, patientId);
        return appointment.map(value -> value.getTimeslot().getDateTime().equals(dateTime)).orElse(false);
    }

    public AppointmentResource getAppointment(Long doctorId, Long patientId, Timestamp dateTime) {
        Optional<Appointment> appointment = appointmentRepository.findByDoctorIdAndPatientId(doctorId, patientId);
        if (appointment.isPresent() && appointment.get().getTimeslot().getDateTime().equals(dateTime)) {
            Appointment a = appointment.get();
            return new AppointmentResource(a.getDoctorId(), a.getPatientId(), a.getTimeslot().getDateTime(),
                                           a.getStatus());
        }

        return null;
    }
}
