package com.altiparmakov.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.altiparmakov.userservice.domain.DoctorProfile;
import com.altiparmakov.userservice.domain.MedicalService;
import com.altiparmakov.userservice.repository.MedicalServiceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalServiceService {
    private final MedicalServiceRepository medicalServiceRepository;

    public List<DoctorProfile> getDoctorsByServiceDescription(String medicalService) {
        MedicalService service = medicalServiceRepository.getByDescription(medicalService)
                .orElseThrow(() -> new RuntimeException());

        return service.getDoctors();
    }
}
