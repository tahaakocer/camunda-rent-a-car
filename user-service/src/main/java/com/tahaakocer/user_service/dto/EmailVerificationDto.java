package com.tahaakocer.user_service.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationDto {
    private boolean isEmailVerified;
}
