package com.devops.accomodation_service.dto;

import com.devops.accomodation_service.model.Accomodation;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccommodationResultDto {

    private Accomodation accommodation;
    private double totalPrice;
    private double pricePerGuest;
    private double distance;

}