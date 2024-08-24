package com.devops.accomodation_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDate startDate;
    private LocalDate endDate;
}
