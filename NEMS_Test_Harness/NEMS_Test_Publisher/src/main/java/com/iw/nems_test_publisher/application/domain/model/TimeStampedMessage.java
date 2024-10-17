package com.iw.nems_test_publisher.application.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeStampedMessage implements Serializable {

    private JsonNode content;
    private Map<String,String> headers;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    private Date timeStamp;

    public TimeStampedMessage(JsonNode message, Map<String,String> headers) {
        this.content = message;
        this.headers = headers;
        this.timeStamp = new Date();
    }

    public TimeStampedMessage(JsonNode message) {
        this.content = message;
        this.headers = new HashMap<String, String>();
        this.timeStamp = new Date();
    }

    public TimeStampedMessage() {
        this.content = JsonNodeFactory.instance.objectNode();
        this.headers = new HashMap<String, String>();;
        this.timeStamp = new Date();
    }
}
