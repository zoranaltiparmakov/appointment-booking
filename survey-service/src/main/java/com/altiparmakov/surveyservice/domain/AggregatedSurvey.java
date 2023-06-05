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

@Entity
@Getter
@NoArgsConstructor
public class AggregatedSurvey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    public AggregatedSurvey(Long doctorId) {
        this.doctorId = doctorId;
    }

    @NaturalId
    private Long doctorId;
    @Setter
    private Double satisfaction;
    @Setter
    @NaturalId
    private Integer cases;
    @Setter
    private Integer patients;
}
