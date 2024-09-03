package com.devops.accomodation_service.controller.internal;

import com.devops.accomodation_service.dto.AccommodationDetails;
import com.devops.accomodation_service.dto.internal.reservation.AccommodationReservation;
import com.devops.accomodation_service.dto.internal.reservation.ReservationDto;
import com.devops.accomodation_service.service.AccomodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/internal/accommodations")
@RequiredArgsConstructor
public class InternalAccommodationController {

    private final AccomodationService accomodationService;

    @PostMapping("/make-reservation")
    AccommodationReservation makeReservationForAccommodation(@RequestBody ReservationDto reservationDto) {
        return accomodationService.makeReservation(reservationDto);
    }

    @DeleteMapping("/delete-users-accommodations/{userId}")
    void deleteUsersAccommodation(@PathVariable String userId) {
        accomodationService.deleteUsersAccommodations(UUID.fromString(userId));
    }

    @GetMapping("host/{accomodationId}")
    UUID getHostId(@PathVariable String accomodationId) {
        return accomodationService.getHostId(UUID.fromString(accomodationId));
    }

    @GetMapping("/{id}")
    AccommodationDetails getAccommodationDetails(@PathVariable UUID id) {
        return accomodationService.getAccommodationDetails(id);

    }
}
