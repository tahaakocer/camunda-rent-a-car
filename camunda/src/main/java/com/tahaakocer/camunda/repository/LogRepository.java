package com.tahaakocer.camunda.repository;

import com.tahaakocer.camunda.model.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
    List<LogEntity> findByProcessInstanceIdAndActivityIdOrderByTimestampDesc(
            String processInstanceId, String activityId);

    List<LogEntity> findByProcessInstanceIdOrderByTimestampDesc(String processInstanceId);
}
