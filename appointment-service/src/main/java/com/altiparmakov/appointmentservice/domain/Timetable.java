package com.altiparmakov.appointmentservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "timetables")
public class Timetable extends BaseEntity {

    @Column(unique = true)
    private Long doctorId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "timetable_id")
    private List<Timeslot> timeslots = new ArrayList<>();

    public Timetable(Long doctorId) {
        this.doctorId = doctorId;
    }
}