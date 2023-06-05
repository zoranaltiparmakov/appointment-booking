package com.altiparmakov.surveyservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestSurveyDto {

    private Long doctorId;
    private Long patientId;
    private String caseDateTime;
}
