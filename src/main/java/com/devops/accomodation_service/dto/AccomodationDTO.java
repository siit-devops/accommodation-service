package com.devops.accomodation_service.dto;

import com.devops.accomodation_service.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccomodationDTO {

    private String name;
    private String description;
    private int minGuestNum;
    private int maxGuestNum;
    private Location location;
    private Set<String> tags;
}
