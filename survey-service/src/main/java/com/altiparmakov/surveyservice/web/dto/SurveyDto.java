package com.altiparmakov.surveyservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SurveyDto {

    private Long doctorId;
    private Long patientId;
    private String caseId;
    private String caseDateTime;
    private Double satisfaction;
}
