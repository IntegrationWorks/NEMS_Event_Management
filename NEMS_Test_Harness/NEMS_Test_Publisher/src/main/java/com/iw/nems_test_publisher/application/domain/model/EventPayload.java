package com.iw.nems_test_publisher.application.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
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
    private String[] payloadStrings;

    // Interval between messages in seconds
    @Positive(message = "interval must be defined as positive decimal number greater than zero")
    private double interval;

}