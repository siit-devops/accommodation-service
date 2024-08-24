package com.devops.accomodation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceDTO {
    private double basePrice;
    private boolean perPerson;
    Set<SeasonalPricingDTO> seasonalPricings;
}
