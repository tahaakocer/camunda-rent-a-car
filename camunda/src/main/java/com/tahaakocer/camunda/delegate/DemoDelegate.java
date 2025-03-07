package com.tahaakocer.camunda.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class DemoDelegate implements JavaDelegate {
    private final RuntimeService runtimeService;

    public DemoDelegate(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }
    private Expression processInstanceId;
    private Expression message;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("transactionId", UUID.randomUUID().toString());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("catchProcess",variables);
        delegateExecution.setVariable("catchProcessInstanceId", processInstance.getId());
        delegateExecution.setVariable("transactionId", variables.get("transactionId"));
        log.info("processInstanceId: " + processInstance.getId());
    }
}
