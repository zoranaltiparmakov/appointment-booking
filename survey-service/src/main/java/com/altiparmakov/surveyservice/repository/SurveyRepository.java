package com.altiparmakov.surveyservice.repository;

import org.springframework.data.repository.CrudRepository;
import com.altiparmakov.surveyservice.domain.Survey;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

public interface SurveyRepository extends CrudRepository<Survey, Long> {
    Iterable<Survey> findAllByIsProcessedIsFalse();

    Collection<Survey> findAllByDoctorId(Long doctorId);
    Optional<Survey> findByDoctorIdAndCaseDateTime(Long doctorId, Timestamp dateTime);
}
