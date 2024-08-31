package com.devops.accomodation_service.controller;

import com.devops.accomodation_service.dto.AccomodationDTO;
import com.devops.accomodation_service.exceptions.NotFoundException;
import com.devops.accomodation_service.model.Accomodation;
import com.devops.accomodation_service.service.AccomodationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accommodation")
@RequiredArgsConstructor
@Slf4j
public class AccomodationController {

    private final AccomodationService accomodationService;

    @GetMapping()
    public List<Accomodation> getAllAccomodations() {
        return accomodationService.findAllAccomodation();
    }

    @GetMapping("/{id}")
    public Accomodation getAccommodationById(@PathVariable UUID id) throws NotFoundException {
        return accomodationService.findOneAccomodation(id);
    }

    @PostMapping()
    public Accomodation create(@RequestBody AccomodationDTO dto) {
        return accomodationService.createAccomodation(dto);
    }

    @PostMapping("/update/{id}")
    public Accomodation update(@PathVariable UUID id, @RequestBody AccomodationDTO dto) {
        return accomodationService.updateAccomodation(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id) {
        accomodationService.deleteAccomodation(id);
    }

    @GetMapping("/host/all")
    public List<Accomodation> getAllAccommodationsForHost(Principal principal) {
        return accomodationService.getAllForHost(UUID.fromString(principal.getName()));
    }
}
