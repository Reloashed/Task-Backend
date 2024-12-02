package org.example.taskbe.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Priority levels for a task")
public enum Priority {
    @Schema(description = "Critical priority level")
    CRITICAL,

    @Schema(description = "Major priority level")
    MAJOR,

    @Schema(description = "Minor priority level")
    MINOR
}
