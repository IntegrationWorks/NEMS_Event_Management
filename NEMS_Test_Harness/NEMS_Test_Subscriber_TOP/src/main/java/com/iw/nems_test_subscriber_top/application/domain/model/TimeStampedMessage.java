package com.iw.nems_test_subscriber_top.application.domain.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeStampedMessage implements Serializable {

    private JsonNode content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    private Date timeStamp;

    public TimeStampedMessage(JsonNode message) {
        this.content = message;
        this.timeStamp = new Date();
    }

    public TimeStampedMessage() {
        this.content = JsonNodeFactory.instance.objectNode();
        this.timeStamp = new Date();
    }
}
