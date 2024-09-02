package com.devops.accomodation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {

    private String locationName;
    private double lon;
    private double lat;
    private int guestsNum;
    private LocalDate startDate;
    private LocalDate endDate;

}
