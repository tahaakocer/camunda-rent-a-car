package com.tahaakocer.user_service.dto;

import lombok.Builder;

@Builder
public class EmailVerificationDto {
    private boolean isEmailVerified;
}
