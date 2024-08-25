package com.devops.accomodation_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne(cascade = CascadeType.ALL)
    private Price price;
    @OneToOne(cascade = CascadeType.ALL)
    private Slot slot;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Slot> unavailableSlots;
}
