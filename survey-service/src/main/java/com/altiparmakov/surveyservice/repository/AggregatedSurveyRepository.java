package com.altiparmakov.surveyservice.repository;

import org.springframework.data.repository.CrudRepository;
import com.altiparmakov.surveyservice.domain.AggregatedSurvey;

import java.util.Optional;

public interface AggregatedSurveyRepository extends CrudRepository<AggregatedSurvey, Long> {
    Optional<AggregatedSurvey> findByDoctorId(Long doctorId);
}
