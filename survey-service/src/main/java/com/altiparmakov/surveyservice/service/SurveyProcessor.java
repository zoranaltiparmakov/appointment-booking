package com.altiparmakov.surveyservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.altiparmakov.surveyservice.client.AppointmentServiceClient;
import com.altiparmakov.surveyservice.client.UserServiceClient;
import com.altiparmakov.surveyservice.domain.AggregatedSurvey;
import com.altiparmakov.surveyservice.domain.Survey;
import com.altiparmakov.surveyservice.repository.AggregatedSurveyRepository;
import com.altiparmakov.surveyservice.repository.SurveyRepository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.OptionalDouble;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@EnableAsync
public class SurveyProcessor {
    private final SurveyRepository surveyRepository;
    private final AggregatedSurveyRepository aggregatedSurveyRepository;

    private final AppointmentServiceClient appointmentServiceClient;
    private final UserServiceClient userServiceClient;

    @Async
    @Scheduled(timeUnit = TimeUnit.MILLISECONDS, fixedDelay = 8)
    @Transactional
    public void processSurvey() {
        CompletableFuture.supplyAsync(() -> {
            surveyRepository.findAllByIsProcessedIsFalse()
                    .forEach(survey -> {
                        // validate that patient that filled this survey really had appointment
                        if (!validate(survey)) {
                            return;
                        }

                        updateDoctorRating(survey);
                        survey.setIsProcessed(true);
                        surveyRepository.save(survey);

                        AggregatedSurvey aggregatedSurvey = aggregatedSurveyRepository.findByDoctorId(
                                survey.getDoctorId())
                                .orElse(new AggregatedSurvey(survey.getDoctorId()));
                        aggregatedSurvey.setPatients(Integer.sum(aggregatedSurvey.getPatients(), 1));
                        aggregatedSurvey.setCases(Integer.sum(aggregatedSurvey.getCases(), 1));
                        Double avgSatisfaction = aggregatedSurvey.getSatisfaction() / aggregatedSurvey.getCases();
                        aggregatedSurvey.setSatisfaction(avgSatisfaction);
                        aggregatedSurveyRepository.save(aggregatedSurvey);
                    });
            return null;
        });
    }

    private void updateDoctorRating(Survey survey) {
        Long doctorId = survey.getDoctorId();
        Collection<Survey> surveys = surveyRepository.findAllByDoctorId(doctorId);
        OptionalDouble avgRating = surveys.stream().mapToDouble(Survey::getSatisfaction).average();

        if (avgRating.isPresent()) {
            userServiceClient.updateRating(String.valueOf(doctorId), avgRating.getAsDouble());
        }
    }

    private boolean validate(Survey survey) {
        Long doctorId = survey.getDoctorId();
        Long patientId = survey.getPatientId();
        Timestamp timestamp = survey.getCaseDateTime();
        return appointmentServiceClient.getAppointment(doctorId, patientId, timestamp) != null;
    }
}
