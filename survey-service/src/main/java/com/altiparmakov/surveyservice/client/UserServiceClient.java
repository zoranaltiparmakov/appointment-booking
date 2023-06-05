package com.altiparmakov.surveyservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PatchMapping(value = "/updateRating/{doctorId}")
    void updateRating(@RequestParam String doctorId, @RequestBody Object rating);
}
