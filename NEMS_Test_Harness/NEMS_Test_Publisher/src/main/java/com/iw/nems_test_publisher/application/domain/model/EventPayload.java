package com.iw.nems_test_publisher.application.domain.model;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class EventPayload {

    @NonNull
    @NotBlank(message= "Topic to publish to cannot be empty")
    public String topic; 

    @NonNull
    @NotEmpty(message = "payload must have at least one element")
    private JsonNode[] payload;

    // Interval between messages in seconds
    @PositiveOrZero(message = "interval must be defined as positive decimal number greater than zero")
    private double interval;

}