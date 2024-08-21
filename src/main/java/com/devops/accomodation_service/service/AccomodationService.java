package com.devops.accomodation_service.service;

import com.devops.accomodation_service.exceptions.NotFoundException;
import com.devops.accomodation_service.model.Accomodation;
import com.devops.accomodation_service.repository.AccomodationRepository;
import com.devops.accomodation_service.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccomodationService {

    private final LocationRepository locationRepository;
    private final AccomodationRepository accomodationRepository;

    public Accomodation createAccomodation(Accomodation accomodation) {
        locationRepository.save(accomodation.getLocation());
        return accomodationRepository.save(accomodation);
    }

    public Accomodation findOneAccomodation(Long id) throws NotFoundException {
        return accomodationRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Accomodation> findAllAccomodation(){
        return accomodationRepository.findAll();
    }

    public void deleteAccomodation(Long id) {
        accomodationRepository.deleteById(id);
    }
}
