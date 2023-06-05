package com.altiparmakov.userservice.repository;

import org.springframework.data.repository.CrudRepository;
import com.altiparmakov.userservice.domain.MedicalService;

import java.util.Optional;

public interface MedicalServiceRepository extends CrudRepository<MedicalService, Long> {
    Optional<MedicalService> getByDescription(String description);
}
