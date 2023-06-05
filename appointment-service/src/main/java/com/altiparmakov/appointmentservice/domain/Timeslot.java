package com.altiparmakov.appointmentservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "timeslots")
public class Timeslot extends BaseEntity {

    private Timestamp dateTime;
    private Boolean isAvailable;

    public Timeslot(Timestamp dateTime) {
        this.dateTime = dateTime;
        this.isAvailable = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Timeslot)) return false;
        return getId() != null && getId().equals(((Timeslot) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}