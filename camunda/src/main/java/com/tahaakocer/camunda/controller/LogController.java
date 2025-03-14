package com.tahaakocer.camunda.controller;

import com.tahaakocer.camunda.dto.GeneralResponse;
import com.tahaakocer.camunda.dto.LogDto;
import com.tahaakocer.camunda.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }
    @GetMapping("/get-by-processInstanceId-and-activityId")
    public ResponseEntity<GeneralResponse> findByProcessInstanceIdAndActivityId(@RequestParam String processInstanceId,@RequestParam String activityId) {
        List<LogDto> logDtos = logService.findByProcessInstanceIdAndActivityId(processInstanceId, activityId);
        return ResponseEntity.ok(GeneralResponse.builder()
                .code(200)
                .message("Log bulundu")
                .data(logDtos)
                .build());

    }
    @GetMapping("/get-all-by-processInstanceId")
    public ResponseEntity<GeneralResponse> findAllByProcessInstanceId(@RequestParam String processInstanceId) {
        List<LogDto> logDtos = logService.findAllByProcessInstanceId(processInstanceId);
        return ResponseEntity.ok(GeneralResponse.builder()
                .code(200)
                .message("Log bulundu")
                .data(logDtos)
                .build());

    }

}
