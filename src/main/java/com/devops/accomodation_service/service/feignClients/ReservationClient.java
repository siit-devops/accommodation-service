package com.devops.accomodation_service.service.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "reservation-service", url = "${reservation-service.ribbon.listOfServers}")
public interface ReservationClient {

    @GetMapping("/api/internal/reservations/unavailable-accomodations")
    List<UUID> getUnavailableAccomodations(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate);

    @DeleteMapping("/api/internal/reservations/deleteByAccommodationId/{id}")
    boolean deleteReservationsByAccommodationId(@PathVariable UUID id);
}

