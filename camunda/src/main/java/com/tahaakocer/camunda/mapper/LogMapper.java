package com.tahaakocer.camunda.mapper;

import com.tahaakocer.camunda.dto.LogDto;
import com.tahaakocer.camunda.model.LogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LogMapper {

    LogMapper INSTANCE = Mappers.getMapper(LogMapper.class);
    LogEntity dtoToEntity(LogDto logDto);
    LogDto entityToDto(LogEntity savedLog);
}
