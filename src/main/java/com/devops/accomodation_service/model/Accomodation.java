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
public class Accomodation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    private String name;
    private String description;
    private int minGuestNum = 0;
    private int maxGuestNum;
    private boolean autoApproveReservation = false;
    @ElementCollection
    private Set<String> tags;
    @ElementCollection
    private Set<String> images;
    @OneToOne(cascade = CascadeType.ALL)
    private Location location;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Availability> availabilities;
}
