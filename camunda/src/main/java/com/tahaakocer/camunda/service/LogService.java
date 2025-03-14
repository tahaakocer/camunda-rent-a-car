package com.tahaakocer.camunda.service;
import com.tahaakocer.camunda.dto.LogDto;
import com.tahaakocer.camunda.mapper.LogMapper;
import com.tahaakocer.camunda.model.LogEntity;
import com.tahaakocer.camunda.repository.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LogService {
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;

    }

    public LogDto save(LogDto logDto) {
        LogEntity logEntity = LogMapper.INSTANCE.dtoToEntity(logDto);
        LogEntity savedLog = logRepository.save(logEntity);
        log.info("LogService - save - Log kaydedildi: {}", savedLog);
        return LogMapper.INSTANCE.entityToDto(savedLog);
    }
    public List<LogDto> findByProcessInstanceIdAndActivityId(String processInstanceId, String activityId) {
        List<LogEntity> logEntities = logRepository.findByProcessInstanceIdAndActivityIdOrderByTimestampDesc(processInstanceId, activityId);
        log.info("LogService - findByProcessInstanceIdAndActivityId - Log bulundu: {}", logEntities.size());
        return logEntities.stream().map(LogMapper.INSTANCE::entityToDto).collect(Collectors.toList());
    }
    public List<LogDto> findAllByProcessInstanceId(String processInstanceId) {
        List<LogEntity> logEntities = logRepository.findByProcessInstanceIdOrderByTimestampDesc(processInstanceId);
        log.info("LogService - findAll - Log bulundu: {}", logEntities.size());
        return logEntities.stream().map(LogMapper.INSTANCE::entityToDto).collect(Collectors.toList());
    }
}
