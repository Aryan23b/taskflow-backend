package com.aryan.task_manager.config;

import com.aryan.task_manager.enums.TaskStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskStatusWebConverter
        implements Converter<String, TaskStatus> {

    @Override
    public TaskStatus convert(String value) {
        return TaskStatus.fromValue(value);
    }
}