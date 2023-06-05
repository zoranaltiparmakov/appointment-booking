package com.altiparmakov.surveyservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import com.altiparmakov.protos.AppointmentEvent;
import com.altiparmakov.surveyservice.client.AppointmentServiceClient;
import com.altiparmakov.surveyservice.client.resource.AppointmentResource;
import com.altiparmakov.surveyservice.client.resource.AppointmentStatus;
import com.altiparmakov.surveyservice.domain.Survey;
import com.altiparmakov.surveyservice.repository.SurveyRepository;
import com.altiparmakov.surveyservice.web.dto.RequestSurveyDto;
import com.altiparmakov.surveyservice.web.dto.SurveyDto;
import com.altiparmakov.surveyservice.util.Utils;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final AppointmentServiceClient appointmentServiceClient;

    public void saveSurvey(SurveyDto surveyDto) {
        Survey survey = new Survey(surveyDto.getSatisfaction(), surveyDto.getCaseId(),
                                   Utils.dateTimeToTimestamp(surveyDto.getCaseDateTime()), surveyDto.getDoctorId(),
                                   surveyDto.getPatientId());
        try {
            surveyRepository.save(survey);
        } catch (DataIntegrityViolationException ex) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,
                                               String.format("Survey was already created for the case %s",
                                                             survey.getCaseId()));
        }
    }

    public RequestSurveyDto getSurvey(Long patientId, Long doctorId, Long medicalServiceId, String timeslot) {
        Optional<Survey> survey = surveyRepository.findByDoctorIdAndCaseDateTime(doctorId, Timestamp.valueOf(timeslot));
        AppointmentResource appointment = appointmentServiceClient.getAppointment(doctorId, patientId,
                                                                                  Timestamp.valueOf(timeslot));
        if (survey.isPresent()) {
            if (!appointment.getStatus().equals(AppointmentStatus.DONE)) {
                throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Appointment is still in progress.");
            }

            return new RequestSurveyDto(doctorId, medicalServiceId, timeslot);
        }

        return null;
    }

    private String generateSurvey(String doctorId, String timeslot, String medicalServiceId) {
        String url = "/survey?doctorId=%s&timeslot=%s&medicalServiceId=%s";
        return url.formatted(doctorId, timeslot, medicalServiceId);
    }

    @RabbitListener(queues = "appointment_finished_queue")
    void listenerAdapter(AppointmentEvent event) {
        generateSurvey(event.getDoctorId(), event.getCaseDateTime(), event.getMedicalService());
    }
}
