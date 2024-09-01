package com.devops.accomodation_service.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String username;
    private String firstname;
    private String lastname;
    private double rating;
}
