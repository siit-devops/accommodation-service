package com.devops.accomodation_service.dto;

import com.devops.accomodation_service.enumerations.DayOfTheWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeasonalPricingDTO {
    private double seasonalPrice;
    private SlotDTO slot;
    private String description;
    private Set<DayOfTheWeek> daysOfTheWeek;
}
