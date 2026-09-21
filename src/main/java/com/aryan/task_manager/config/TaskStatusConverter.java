package com.aryan.task_manager.config;

import com.aryan.task_manager.enums.TaskStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TaskStatusConverter
        implements AttributeConverter<TaskStatus, String> {

    @Override
    public String convertToDatabaseColumn(TaskStatus status) {
        return status == null ? null : status.name();
    }

    @Override
    public TaskStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }

        return "COMPLETE".equalsIgnoreCase(value)
                ? TaskStatus.COMPLETED
                : TaskStatus.valueOf(value);
    }
}