package com.altiparmakov.surveyservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Getter
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Setter
    private Double satisfaction;
    @Setter
    @NaturalId
    private String caseId;
    @Setter
    private Timestamp caseDateTime;
    @Setter
    private Long doctorId;
    @Setter
    private Long patientId;
    @Setter
    private Boolean isProcessed = Boolean.FALSE;

    public Survey(Double satisfaction, String caseId, Timestamp caseDateTime, Long doctorId, Long patientId) {
        this.satisfaction = satisfaction;
        this.caseId = caseId;
        this.caseDateTime = caseDateTime;
        this.doctorId = doctorId;
        this.patientId = patientId;
    }
}
