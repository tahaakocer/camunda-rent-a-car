package com.tahaakocer.camunda.delegate;

import com.tahaakocer.camunda.utils.VariableUtils;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GenericSendMessageDelegate implements JavaDelegate {
    private final RuntimeService runtimeService;

    private Expression messageName;
    private Expression variables;

    public GenericSendMessageDelegate(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String variablesStr = (String) variables.getValue(delegateExecution);
        String messageNameStr = (String) messageName.getValue(delegateExecution);

        List<ProcessInstance> instances = runtimeService
                .createProcessInstanceQuery()
                .variableValueEquals(variablesStr, delegateExecution.getVariable(variablesStr))
                .list();

        if (instances.isEmpty()) {
            throw new BpmnError("404", "No process instance found");
        }

        for (ProcessInstance instance : instances) {
            if(!instance.getProcessInstanceId().equals(delegateExecution.getProcessInstanceId())) {
                runtimeService.createMessageCorrelation(messageNameStr)
                        .processInstanceId(instance.getProcessInstanceId())
                        .correlate();
            }

        }
    }
}
