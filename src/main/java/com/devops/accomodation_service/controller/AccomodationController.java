package com.devops.accomodation_service.controller;

import com.devops.accomodation_service.exceptions.NotFoundException;
import com.devops.accomodation_service.model.Accomodation;
import com.devops.accomodation_service.service.AccomodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccomodationController {

    private final AccomodationService accomodationService;

    @GetMapping()
    public List<Accomodation> getAllAccomodations() {
        return accomodationService.findAllAccomodation();
    }

    @GetMapping("/{id}")
    public Accomodation getAccommodationById(@PathVariable Long id) throws NotFoundException {
        return accomodationService.findOneAccomodation(id);
    }

    @PostMapping()
    public Accomodation create(@RequestBody Accomodation dto) {
        return accomodationService.createAccomodation(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        accomodationService.deleteAccomodation(id);
    }
}
