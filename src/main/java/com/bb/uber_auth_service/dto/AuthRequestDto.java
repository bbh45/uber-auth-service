package com.bb.uber_auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequestDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
