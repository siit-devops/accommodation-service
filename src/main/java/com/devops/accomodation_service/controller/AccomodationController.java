package com.devops.accomodation_service.controller;

import com.devops.accomodation_service.dto.AccommodationResultDto;
import com.devops.accomodation_service.dto.AccommodationWithHostDto;
import com.devops.accomodation_service.dto.AccomodationDTO;
import com.devops.accomodation_service.dto.SearchDto;
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

    @GetMapping("/full/{id}")
    public Accomodation getAccomodation(@PathVariable UUID id) {
        return accomodationService.getAccommodation(id);
    }

    @GetMapping("/{id}")
    public AccommodationWithHostDto getAccommodationById(@PathVariable UUID id) throws NotFoundException {
        return accomodationService.getAccommodationWithHostById(id);
    }

    @PostMapping()
    public Accomodation create(@RequestBody AccomodationDTO dto, Principal principal) {
        return accomodationService.createAccomodation(dto, UUID.fromString(principal.getName()));
    }

    @PutMapping("/update/{id}")
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

    @GetMapping("/search")
    public List<AccommodationResultDto> search(SearchDto searchParams){
        return accomodationService.search(searchParams);
    }
}
