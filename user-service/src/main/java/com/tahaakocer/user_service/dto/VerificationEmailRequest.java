package com.tahaakocer.user_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationEmailRequest {
    private String keycloakUserId;
}
