package com.iw.nems_test_publisher.application.domain.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PayloadMessage {

    @NonNull
    @NotEmpty(message = "content must have at least one element")
    private JsonNode content;

    @JsonProperty("add-headers")
    private Map<String, String> headers;

    public PayloadMessage(){
        content = JsonNodeFactory.instance.objectNode();
        headers = new HashMap<String,String>();
    }

    public String toString(){
        return content.toPrettyString();
    }
}
