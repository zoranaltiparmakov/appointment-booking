package com.altiparmakov.surveyservice.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.altiparmakov.surveyservice.web.dto.RequestSurveyDto;
import com.altiparmakov.surveyservice.web.dto.SurveyDto;
import com.altiparmakov.surveyservice.service.SurveyService;

@RestController
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping(value = "/survey/submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void submitSurvey(@RequestBody SurveyDto surveyDto) {
        surveyService.saveSurvey(surveyDto);
    }

    @GetMapping(value = "/survey", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestSurveyDto> getSurvey(@RequestHeader("userId") Long patientId, @RequestParam Long doctorId, @RequestParam Long medicalServiceId, @RequestParam String timeslot) {
        RequestSurveyDto surveyDto = surveyService.getSurvey(patientId, doctorId, medicalServiceId, timeslot);
        return ResponseEntity.ok(surveyDto);
    }
}
