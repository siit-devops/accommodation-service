package com.devops.accomodation_service.model;

import com.devops.accomodation_service.enumerations.DayOfTheWeek;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class SeasonalPricing {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private double seasonalPrice;
    @OneToOne(cascade = CascadeType.ALL)
    private Slot slot;
    private String description;
    @ElementCollection
    private Set<DayOfTheWeek> daysOfTheWeek;
}
