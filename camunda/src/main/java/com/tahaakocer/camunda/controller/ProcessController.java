package com.tahaakocer.camunda.controller;


import com.tahaakocer.camunda.service.ProcessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/process")
public class ProcessController {
    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    @PostMapping("/start")
    ResponseEntity<String> startProcess(@RequestParam String processKey) {
        processService.startProcess(processKey);
        return ResponseEntity.ok("process started");
    }
    @GetMapping("/viewer")
    public String showBpmnViewer() {
        return "redirect:/viewer/index.html";
    }
}
