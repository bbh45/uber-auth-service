package com.bb.uber_auth_service.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import com.bb.uber_auth_service.dto.AuthRequestDto;
import com.bb.uber_auth_service.dto.AuthResponseDto;
import com.bb.uber_auth_service.dto.PassengerSignupRequestDto;
import com.bb.uber_auth_service.dto.PassengerSignupResponseDto;
import com.bb.uber_auth_service.services.AuthService;
import com.bb.uber_auth_service.services.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtService jwtService){
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup/passenger")
    public ResponseEntity<PassengerSignupResponseDto> signUp(@RequestBody @Valid PassengerSignupRequestDto passengerSignupRequestDto){
        PassengerSignupResponseDto response = authService.signupPassenger(passengerSignupRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/signin/passenger")
    public ResponseEntity<AuthResponseDto> signIn(@RequestBody @Valid AuthRequestDto authRequestDto, HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));
        if(authentication.isAuthenticated()){
            String jwtToken = jwtService.createToken(authRequestDto.getEmail());
            System.out.println("Token : "+jwtToken);
            ResponseCookie cookie = ResponseCookie.from("JwtToken", jwtToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(7*24*3600)
                    .build();
            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return new ResponseEntity<>(AuthResponseDto.builder().isSuccess(true).build(), HttpStatus.OK);
        }else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(HttpServletRequest request, HttpServletResponse response){
        System.out.println("Inside validate controller");
        for(Cookie cookie: request.getCookies()){
            System.out.println(cookie.getName() + " " + cookie.getValue());
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
