package com.devops.accomodation_service.dto.internal.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationReservation {
    boolean autoApproveReservation;
    double totalPrice;
}
