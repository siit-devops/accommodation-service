package com.devops.accomodation_service.model;

import jakarta.persistence.*;
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
@Entity
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private double basePrice;
    private boolean perPerson;
    @OneToMany(cascade = CascadeType.ALL)
    Set<SeasonalPricing> seasonalPricings;
}
