package com.tahaakocer.camunda.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String activityId;
    private String stepName;
    private String serviceName;
    private String processInstanceId;

    @Column(columnDefinition = "TEXT")
    private String requestBody;

    @Column(columnDefinition = "TEXT")
    private String responseBody;

    private LocalDateTime timestamp;


}
