package com.devops.accomodation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDTO {
    private String name;
    private String fullAddress;
    private double lon;
    private double lat;
}
