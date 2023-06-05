package com.altiparmakov.userservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "doctors")
@AttributeOverride(name = "id", column = @Column(name = "doctor_id"))
public class DoctorProfile extends BaseProfile {

    private Double rating;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "doctors_services",
            joinColumns = {@JoinColumn(name = "doctor_id")},
            inverseJoinColumns = {@JoinColumn(name = "medicalservice_id")}
    )
    private List<MedicalService> medicalServices;
}
