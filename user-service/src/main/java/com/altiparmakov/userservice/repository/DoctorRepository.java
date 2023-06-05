package com.altiparmakov.userservice.repository;

import org.springframework.data.repository.CrudRepository;
import com.altiparmakov.userservice.domain.DoctorProfile;

import java.util.Optional;

public interface DoctorRepository extends CrudRepository<DoctorProfile, Long> {
    Optional<DoctorProfile> findDoctorProfileById(Long profileId);
}
