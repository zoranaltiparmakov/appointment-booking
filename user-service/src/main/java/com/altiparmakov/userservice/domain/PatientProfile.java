package com.altiparmakov.userservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "patients")
@AttributeOverride(name = "id", column = @Column(name = "patient_id"))
public class PatientProfile extends BaseProfile {

    @NaturalId
    private Long healthInsuranceNumber;
}
