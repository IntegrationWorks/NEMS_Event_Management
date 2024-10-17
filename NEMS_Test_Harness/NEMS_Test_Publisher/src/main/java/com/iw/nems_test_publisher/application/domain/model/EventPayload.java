package com.iw.nems_test_publisher.application.domain.model;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class EventPayload {

    @NonNull
    @NotBlank(message= "Topic to publish to cannot be empty")
    public String topic; 

    // 
    private PayloadMessage[] messages = {};

    // Interval between messages in seconds
    @PositiveOrZero(message = "interval must be defined as positive decimal number greater than zero")
    private double interval;

    // BACKWARDS COMPATIBLE VARS
    
    // Stores Content of all messages to be sent
    private JsonNode[] payload;

}

