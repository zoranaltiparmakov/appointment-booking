package com.altiparmakov.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import com.altiparmakov.apigateway.filter.AuthFilter;
import com.altiparmakov.apigateway.filter.DoctorAuthFilter;

@Configuration
@RequiredArgsConstructor
public class RouterConfig {

    private static final String APPOINTMENT_SERVICE_URI = "lb://appointment-service";
    private static final String SURVEY_SERVICE_URI = "lb://survey-service";
    private static final String APPOINTMENTS_API_PATH = "/api/appointments/**";
    private static final String SURVEY_API_PATH = "/api/survey/**";
    private final AuthFilter authFilter;
    private final DoctorAuthFilter doctorAuthFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("create-timetable", p -> p
                        .path("/api/timetable")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(filter -> filter
                                .filter(authFilter)
                                .filter(doctorAuthFilter)
                                .stripPrefix(1))
                        .uri(APPOINTMENT_SERVICE_URI))
                .route("view-free-timeslots", p -> p
                        .path("/api/timeslots")
                        .and()
                        .method(HttpMethod.GET)
                        .filters(filter -> filter
                                .filter(authFilter)
                                .stripPrefix(1))
                        .uri(APPOINTMENT_SERVICE_URI))
                .route("appointment", p -> p
                        .path(APPOINTMENTS_API_PATH)
                        .filters(filter -> filter
                                .filter(authFilter)
                                .stripPrefix(1))
                        .uri(APPOINTMENT_SERVICE_URI))
                .route("submit-survey", p -> p
                        .path(SURVEY_API_PATH)
                        .filters(filter -> filter
                                .filter(authFilter)
                                .stripPrefix(1))
                        .uri(SURVEY_SERVICE_URI))
                .build();
    }
}
