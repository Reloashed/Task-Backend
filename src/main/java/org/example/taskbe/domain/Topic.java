package org.example.taskbe.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Topics associated with a task")
public enum Topic {
    @Schema(description = "Mathematics topic")
    MATHEMATICS,

    @Schema(description = "English topic")
    ENGLISH,

    @Schema(description = "French topic")
    FRENCH,

    @Schema(description = "German topic")
    GERMAN
}
