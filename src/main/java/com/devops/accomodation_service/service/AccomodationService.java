package com.devops.accomodation_service.service;

import com.devops.accomodation_service.dto.AvailabilityDTO;
import com.devops.accomodation_service.dto.SlotDTO;
import com.devops.accomodation_service.exceptions.NotFoundException;
import com.devops.accomodation_service.model.Accomodation;
import com.devops.accomodation_service.model.Availability;
import com.devops.accomodation_service.model.Slot;
import com.devops.accomodation_service.repository.AccomodationRepository;
import com.devops.accomodation_service.repository.LocationRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AccomodationService {

    private final LocationRepository locationRepository;
    private final AccomodationRepository accomodationRepository;

    public Accomodation createAccomodation(Accomodation accomodation) {
        locationRepository.save(accomodation.getLocation());
        return accomodationRepository.save(accomodation);
    }

    public Accomodation findOneAccomodation(UUID id) throws NotFoundException {
        return accomodationRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Accomodation> findAllAccomodation(){
        return accomodationRepository.findAll();
    }

    public void deleteAccomodation(UUID id) {
        accomodationRepository.deleteById(id);
    }

    public boolean checkAvailability(AvailabilityDTO dto) {
        Accomodation accomodation = findOneAccomodation(dto.getId());
        Slot s = new Slot();
        s.setStartDate(dto.getSlot().getStartDate());
        s.setEndDate(dto.getSlot().getEndDate());

        for (Availability availability : accomodation.getAvailabilities()) {
            if (!isSlotInbetween(s, availability.getSlot())) {
                continue;
            }

            for (Slot slot : availability.getUnavailableSlots())
            {
                if (doSlotsOverlap(s, slot)) {
                    return false;
                }
            }
        }

        // TODO check with reservation service! Can change only for ranges with no ongoing reservations!

        return true;
    }

    private boolean isSlotInbetween(Slot s1, Slot s2) {
        return (s1.getStartDate().isEqual(s2.getStartDate()) || s1.getStartDate().isAfter(s2.getStartDate())) &&
                (s1.getEndDate().isEqual(s2.getEndDate()) || s1.getEndDate().isBefore(s2.getEndDate()));
    }

    public boolean doSlotsOverlap(Slot s1, Slot s2) {
        return s1.getStartDate().isBefore(s2.getEndDate().plusDays(1)) &&
                s1.getEndDate().isAfter(s2.getStartDate().minusDays(1));
    }
}
