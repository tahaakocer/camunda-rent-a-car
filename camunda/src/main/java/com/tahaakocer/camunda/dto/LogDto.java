package com.tahaakocer.camunda.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogDto {
    private Long id;
    private String activityId;
    private String stepName;
    private String serviceName;
    private String processInstanceId;
    private String requestBody;
    private String responseBody;
    private LocalDateTime timestamp;
}
