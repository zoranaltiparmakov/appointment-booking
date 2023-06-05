package com.altiparmakov.userservice.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.altiparmakov.userservice.domain.DoctorProfile;
import com.altiparmakov.userservice.web.dto.DoctorDto;
import com.altiparmakov.userservice.service.MedicalServiceService;
import com.altiparmakov.userservice.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserServiceController {

    private final MedicalServiceService medicalServiceService;
    private final UserService userService;

    @GetMapping(value = "/getDoctorsByService")
    public List<DoctorDto> getDoctorsByService(String medicalService) {
        List<DoctorProfile> doctors = medicalServiceService.getDoctorsByServiceDescription(medicalService);
        return doctors.parallelStream()
                .map(doctor ->
                             new DoctorDto(doctor.getId(), doctor.getFullName(), doctor.getRating()))
                .toList();
    }

    @PatchMapping(value = "/updateRating/{doctorId}")
    public void updateRating(@RequestParam String doctorId, @RequestBody Object rating) {
        userService.updateRating(Long.parseLong(doctorId), Double.parseDouble((String) rating));
    }
}
