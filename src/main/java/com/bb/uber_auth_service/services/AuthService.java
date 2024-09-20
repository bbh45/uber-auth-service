package com.bb.uber_auth_service.services;

import com.bb.uber_auth_service.dto.PassengerSignupRequestDto;
import com.bb.uber_auth_service.dto.PassengerSignupResponseDto;

import com.bb.uber_auth_service.repositories.PassengerRepository;
import com.bb.uber_entity_service.models.Passenger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final PassengerRepository passengerRepository;

    public AuthService(PasswordEncoder passwordEncoder, PassengerRepository passengerRepository){
        this.passwordEncoder = passwordEncoder;
        this.passengerRepository = passengerRepository;
    }
    public PassengerSignupResponseDto signupPassenger(PassengerSignupRequestDto passengerSignupRequestDto) {
        Passenger passenger = Passenger.builder()
                .name(passengerSignupRequestDto.getName())
                .email(passengerSignupRequestDto.getEmail())
                .password(passwordEncoder.encode(passengerSignupRequestDto.getPassword()))
                .phoneNumber(passengerSignupRequestDto.getPhoneNumber())
                .build();
        passenger = passengerRepository.save(passenger);
        return PassengerSignupResponseDto.from(passenger);
    }
}
