package com.altiparmakov.appointmentservice.client.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping(value = "/getDoctorsByService")
    List<DoctorResource> getDoctorsByService(@RequestParam(name = "medicalService") String medicalService);
}
