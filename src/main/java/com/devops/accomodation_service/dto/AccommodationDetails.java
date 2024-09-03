package com.devops.accomodation_service.dto;

import lombok.*;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccommodationDetails {
    private String name;
    private Set<String> images;
}
