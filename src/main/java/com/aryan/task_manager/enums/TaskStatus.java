package com.aryan.task_manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    COMPLETED;

    @JsonCreator
    public static TaskStatus fromValue(String value) {
        if ("COMPLETE".equalsIgnoreCase(value)) {
            return COMPLETED;
        }

        return value == null ? null : valueOf(value.toUpperCase());
    }
}
