package com.altiparmakov.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.altiparmakov.userservice.domain.DoctorProfile;
import com.altiparmakov.userservice.repository.DoctorRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final DoctorRepository doctorRepository;

    public void updateRating(long profileId, double rating) {
        DoctorProfile doctorProfile = doctorRepository.findDoctorProfileById(profileId)
                .orElseThrow(() -> new RuntimeException());
        doctorProfile.setRating(rating);
    }
}
