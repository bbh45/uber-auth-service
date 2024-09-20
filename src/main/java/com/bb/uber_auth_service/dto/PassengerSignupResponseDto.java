package com.bb.uber_auth_service.dto;

import com.bb.uber_entity_service.models.Passenger;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerSignupResponseDto {
    private String id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private Date createdAt;

    public static PassengerSignupResponseDto from(Passenger p){
        return PassengerSignupResponseDto.builder()
                .id(p.getId().toString())
                .createdAt(p.getCreatedAt())
                .email(p.getEmail())
                .password(p.getPassword())
                .phoneNumber(p.getPhoneNumber())
                .name(p.getName())
                .build();
    }
}
