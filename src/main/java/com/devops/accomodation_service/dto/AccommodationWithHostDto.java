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
public class AccommodationWithHostDto {
    private UUID id;
    private UserDto host;
    private String name;
    private String description;
    private int minGuestNum;
    private int maxGuestNum;
    private Set<String> tags;
    private Set<String> images;
    private LocationDTO location;
}