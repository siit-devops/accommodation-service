package com.devops.accomodation_service.service;

import com.devops.accomodation_service.dto.*;
import com.devops.accomodation_service.dto.internal.reservation.ReservationDto;
import com.devops.accomodation_service.enumerations.DayOfTheWeek;
import com.devops.accomodation_service.dto.internal.reservation.AccommodationReservation;
import com.devops.accomodation_service.exceptions.NotFoundException;
import com.devops.accomodation_service.model.*;
import com.devops.accomodation_service.repository.AccomodationRepository;
import com.devops.accomodation_service.service.feignClients.ReservationClient;
import com.devops.accomodation_service.service.feignClients.UserClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccomodationService {

    private final double EARTH_RADIUS = 6371000; // Earth's radius in meters
    private final AccomodationRepository accomodationRepository;
    private final UserClient userClient;
    private final ReservationClient reservationClient;

    public AccomodationService(AccomodationRepository accomodationRepository, UserClient userClient, ReservationClient reservationClient) {
        this.accomodationRepository = accomodationRepository;
        this.userClient = userClient;
        this.reservationClient = reservationClient;
    }

    public Accomodation createAccomodation(AccomodationDTO dto) {
        Accomodation accomodation = new Accomodation();
        fillAccomodation(accomodation, dto);

        return accomodationRepository.save(accomodation);
    }

    public Accomodation updateAccomodation(UUID id, AccomodationDTO dto) {
        Accomodation accomodation = findOneAccomodation(id);
        fillAccomodation(accomodation, dto);

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
                for (SeasonalPricing sp : availability.getPrice().getSeasonalPricings()) {
                    if (isSlotInbetween(currentDaySlot, sp.getSlot()) && (
                            sp.getDaysOfTheWeek().contains(DayOfTheWeek.NOT_SPECIFIED) ||
                                    sp.getDaysOfTheWeek().contains(currentDate.getDayOfWeek().getValue()))) {
                        priceOnDay = sp.getSeasonalPrice();
                    }
                }

                if (availability.getPrice().isPerPerson()) {
                    priceOnDay *= numberOfGuests;
                }

                finalPrice += priceOnDay;

                currentDate = currentDate.plusDays(1);
            }

            break;
        }

        return finalPrice;
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

    @Transactional
    public void deleteUsersAccommodations(UUID hostId) {
        accomodationRepository.deleteAllByUserId(hostId);
    }

    private Accomodation fillAccomodation(Accomodation accomodation, AccomodationDTO dto) {
        Set<Availability> availabilitySet = new HashSet<>();
        for (AvailabilityDTO availabilityDTO : dto.getAvailabilities()) {
            availabilitySet.add(createAvailability(availabilityDTO));
        }

        accomodation.setName(dto.getName());
        accomodation.setDescription(dto.getDescription());
        accomodation.setMinGuestNum(dto.getMinGuestNum());
        accomodation.setMaxGuestNum(dto.getMaxGuestNum());
        accomodation.setLocation(createLocation(dto.getLocation()));
        accomodation.setTags(dto.getTags());
        accomodation.setImages(dto.getImages());
        accomodation.setAutoApproveReservation(dto.isAutoApproveReservation());
        accomodation.setAvailabilities(availabilitySet);
        return accomodation;
    }

    private boolean isSlotInbetween(Slot s1, Slot s2) {
        return (s1.getStartDate().isEqual(s2.getStartDate()) || s1.getStartDate().isAfter(s2.getStartDate())) &&
                (s1.getEndDate().isEqual(s2.getEndDate()) || s1.getEndDate().isBefore(s2.getEndDate()));
    }

    public boolean doSlotsOverlap(Slot s1, Slot s2) {
        return s1.getStartDate().isBefore(s2.getEndDate().plusDays(1)) &&
                s1.getEndDate().isAfter(s2.getStartDate().minusDays(1));
    }

    private Location createLocation(LocationDTO locationDTO) {
        Location location = new Location();
        location.setName(locationDTO.getName());
        location.setFullAddress(locationDTO.getFullAddress());
        location.setLon(locationDTO.getLon());
        location.setLat(locationDTO.getLat());
        return location;
    }

    private Slot createSlot(SlotDTO slotDTO) {
        Slot slot = new Slot();
        slot.setStartDate(slotDTO.getStartDate());
        slot.setEndDate(slotDTO.getEndDate());
        return slot;
    }

    private SeasonalPricing createSeasonalPricing(SeasonalPricingDTO seasonalPricingDTO) {
        SeasonalPricing seasonalPricing = new SeasonalPricing();
        seasonalPricing.setSlot(createSlot(seasonalPricingDTO.getSlot()));
        seasonalPricing.setSeasonalPrice(seasonalPricingDTO.getSeasonalPrice());
        seasonalPricing.setDaysOfTheWeek(seasonalPricingDTO.getDaysOfTheWeek());
        return seasonalPricing;
    }

    private Price createPrice(PriceDTO priceDTO) {
        Price price = new Price();
        price.setBasePrice(priceDTO.getBasePrice());
        price.setPerPerson(priceDTO.isPerPerson());
        price.setSeasonalPricings(new HashSet<>());
        for (SeasonalPricingDTO seasonalPricingDTO : priceDTO.getSeasonalPricings()) {
            price.getSeasonalPricings().add(createSeasonalPricing(seasonalPricingDTO));
        }
        return price;
    }

    private Availability createAvailability(AvailabilityDTO availabilityDTO) {
        Availability availability = new Availability();
        availability.setSlot(createSlot(availabilityDTO.getSlot()));
        availability.setUnavailableSlots(new HashSet<>());
        for (SlotDTO slotDTO : availabilityDTO.getUnavailableSlots()) {
            availability.getUnavailableSlots().add(createSlot(slotDTO));
        }
        availability.setPrice(createPrice(availabilityDTO.getPrice()));
        return availability;
    }

    public List<Accomodation> getAllForHost(UUID hostId) {
        return accomodationRepository.findAllByUserId(hostId);
    }

    public AccommodationWithHostDto getAccommodationWithHostById(UUID id) {
        Accomodation accommodation = findOneAccomodation(id);
        UserDto host = userClient.findById(accommodation.getUserId());

        return AccommodationWithHostDto.builder()
                .id(accommodation.getId())
                .name(accommodation.getName())
                .description(accommodation.getDescription())
                .minGuestNum(accommodation.getMinGuestNum())
                .maxGuestNum(accommodation.getMaxGuestNum())
                .tags(accommodation.getTags())
                .images(accommodation.getImages())
                .location(LocationDTO.builder()
                        .name(accommodation.getLocation().getName())
                        .lon(accommodation.getLocation().getLon())
                        .lat(accommodation.getLocation().getLat())
                        .fullAddress(accommodation.getLocation().getFullAddress())
                        .build())
                .host(host)
                .build();
    }

    public UUID getHostId(UUID accomodationId) {
        return findOneAccomodation(accomodationId).getUserId();
    }

    public AccommodationDetails getAccommodationDetails(UUID id) {
        var accommodation = accomodationRepository.findById(id).orElseThrow(() -> new NotFoundException("Accommodation not found"));

        return AccommodationDetails.builder()
                .images(accommodation.getImages())
                .name(accommodation.getName())
                .build();
    }

    public List<AccommodationResultDto> search(SearchDto searchParams) {
        List<AccommodationResultDto> result = new ArrayList<>();

        List<UUID> unavailableAccommodations = reservationClient.getUnavailableAccomodations(searchParams.getStartDate(),
                searchParams.getEndDate());

        if(unavailableAccommodations.isEmpty())
            unavailableAccommodations.add(UUID.fromString("-1"));

        List<Accomodation> filteredAccommodations = accomodationRepository
                .filter(searchParams.getGuestsNum(), unavailableAccommodations);

        for (Accomodation accomodation : filteredAccommodations) {
            if (checkAvailability(accomodation.getId(), searchParams.getStartDate(), searchParams.getEndDate(), searchParams.getGuestsNum()))
            {
                double totalPrice = calculatePrice(accomodation.getId(), searchParams.getStartDate(), searchParams.getEndDate(), searchParams.getGuestsNum());
                result.add(AccommodationResultDto.builder()
                        .accommodation(accomodation)
                        .totalPrice(totalPrice)
                        .pricePerGuest(totalPrice / searchParams.getGuestsNum())
                        .distance(calculateDistance(searchParams.getLat(), searchParams.getLon(),
                                accomodation.getLocation().getLat(), accomodation.getLocation().getLon()))
                        .build()
                );
            }
        }

        return result;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
