package com.devops.accomodation_service.controller;

import com.devops.accomodation_service.dto.AvailabilityDTO;
import com.devops.accomodation_service.service.AccomodationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
@Slf4j
public class AvailabilityController {

    private final AccomodationService accomodationService;

    @PostMapping("/accommodation/check-availability")
    public boolean checkAvailability(@Valid @RequestBody AvailabilityDTO dto) {
        return accomodationService.checkAvailability(dto);
    }

    @PutMapping("/update/{id}")
    public boolean updateAvailability(@NotNull @RequestBody AvailabilityDTO dto) {
        // TODO check with reservation service! Can change only for ranges with no ongoing reservations!
        return true;
    }
}
