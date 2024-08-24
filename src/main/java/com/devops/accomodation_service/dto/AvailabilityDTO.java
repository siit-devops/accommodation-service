package com.devops.accomodation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailabilityDTO {
    private UUID id;
    private SlotDTO slot;
    private PriceDTO price;
    private Set<SlotDTO> unavailableSlots;
}
