package com.tahaakocer.user_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ValidFormResponse {
    private boolean isFormValid;
}
