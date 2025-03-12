package com.tahaakocer.camunda.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BpmnController {

    @GetMapping("/viewer")
    public String showBpmnViewer(@RequestParam(name = "processInstanceId", required = false) String processInstanceId, Model model) {
        model.addAttribute("processInstanceId", processInstanceId);
        return "index"; // src/main/resources/templates/index.html şablonunu işaret eder
    }
}