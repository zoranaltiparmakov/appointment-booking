package com.altiparmakov.surveyservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.altiparmakov.surveyservice.client.resource.AppointmentResource;

import java.sql.Timestamp;

@FeignClient(name = "appointment-service")
public interface AppointmentServiceClient {

    @GetMapping(value = "/appointments")
    AppointmentResource getAppointment(@RequestParam(name = "doctorId") Long doctorId,
                                       @RequestParam(name = "patientId") Long patientId,
                                       @RequestParam(name = "dateTime") Timestamp dateTime);
}
