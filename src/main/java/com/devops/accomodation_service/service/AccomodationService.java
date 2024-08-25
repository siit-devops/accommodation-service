package com.devops.accomodation_service.service;

import com.devops.accomodation_service.dto.internal.reservation.ReservationDto;
import com.devops.accomodation_service.enumerations.DayOfTheWeek;
import com.devops.accomodation_service.dto.internal.reservation.AccommodationReservation;
import com.devops.accomodation_service.exceptions.NotFoundException;
import com.devops.accomodation_service.model.Accomodation;
import com.devops.accomodation_service.model.Availability;
import com.devops.accomodation_service.model.SeasonalPricing;
import com.devops.accomodation_service.model.Slot;
import com.devops.accomodation_service.repository.AccomodationRepository;
import com.devops.accomodation_service.repository.LocationRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<Accomodation> findAllAccomodation() {
        return accomodationRepository.findAll();
    }

    public void deleteAccomodation(UUID id) {
        accomodationRepository.deleteById(id);
    }

    public boolean checkAvailability(UUID accomodationId, LocalDate startDate, LocalDate endDate, int numberOfGuests) {
        Accomodation accomodation = findOneAccomodation(accomodationId);

        if (numberOfGuests != 0 && (numberOfGuests < accomodation.getMinGuestNum() || numberOfGuests > accomodation.getMaxGuestNum())) {
            return false;
        }

        Slot s = new Slot();
        s.setStartDate(startDate);
        s.setEndDate(endDate);

        for (Availability availability : accomodation.getAvailabilities()) {
            if (isSlotInbetween(s, availability.getSlot())) {
                for (Slot slot : availability.getUnavailableSlots()) {
                    if (doSlotsOverlap(s, slot)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public double calculatePrice(UUID accomodationId, LocalDate startDate, LocalDate endDate, int numberOfGuests) {
        Accomodation accomodation = findOneAccomodation(accomodationId);
        Slot s = new Slot();
        s.setStartDate(startDate);
        s.setEndDate(endDate);
        LocalDate currentDate = LocalDate.parse(startDate.toString());
        double finalPrice = 0;

        for (Availability availability : accomodation.getAvailabilities()) {
            if (!isSlotInbetween(s, availability.getSlot())) {
                continue;
            }

            while (currentDate.isBefore(endDate)) {
                double priceOnDay = availability.getPrice().getBasePrice();
                Slot currentDaySlot = new Slot();
                currentDaySlot.setStartDate(currentDate);
                currentDaySlot.setEndDate(currentDate);
                for (SeasonalPricing sp : availability.getPrice().getSeasonalPricings())
                {
                    if (isSlotInbetween(currentDaySlot, sp.getSlot()) && (
                            sp.getDaysOfTheWeek().contains(DayOfTheWeek.NOT_SPECIFIED) ||
                            sp.getDaysOfTheWeek().contains(currentDate.getDayOfWeek().getValue())))
                    {
                        priceOnDay = sp.getSeasonalPrice();
                    }
                }

                if (availability.getPrice().isPerPerson())
                {
                    priceOnDay *= numberOfGuests;
                }

                finalPrice += priceOnDay;

                currentDate = currentDate.plusDays(1);
            }

            break;
        }

        return finalPrice;
    }

    private boolean isSlotInbetween(Slot s1, Slot s2) {
        return (s1.getStartDate().isEqual(s2.getStartDate()) || s1.getStartDate().isAfter(s2.getStartDate())) &&
                (s1.getEndDate().isEqual(s2.getEndDate()) || s1.getEndDate().isBefore(s2.getEndDate()));
    }

    public boolean doSlotsOverlap(Slot s1, Slot s2) {
        return s1.getStartDate().isBefore(s2.getEndDate().plusDays(1)) &&
                s1.getEndDate().isAfter(s2.getStartDate().minusDays(1));
    }

    public AccommodationReservation makeReservation(ReservationDto reservationDto) {
        var accommodation = accomodationRepository.findById(reservationDto.getAccommodationId()).orElseThrow(NotFoundException::new);

        var available = checkAvailability(
                reservationDto.getAccommodationId(),
                reservationDto.getReservationStart(),
                reservationDto.getReservationEnd(),
                reservationDto.getGuestNumber()
        );
        if (available) {
            var totalPrice = calculatePrice(
                    accommodation.getId(),
                    reservationDto.getReservationStart(),
                    reservationDto.getReservationEnd(),
                    reservationDto.getGuestNumber()
            );
            return AccommodationReservation.builder()
                    .autoApproveReservation(accommodation.isAutoApproveReservation())
                    .totalPrice(totalPrice)
                    .build();
        }
        return null;
    }
}
