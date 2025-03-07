package com.tahaakocer.user_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralResponse {
    private int code;
    private String message;
    private Object data;
}
