package com.iw.nems_test_subscriber.application.domain.model;

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

    private String topic;
    private JsonNode payload;
    private int interval;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    private Date timeStamp;

    public TimeStampedMessage(String topic, JsonNode payload, int interval, Date timeStamp) {
        this.topic = topic;
        this.payload = payload;
        this.interval = interval;
        this.timeStamp = timeStamp;
    }

    public TimeStampedMessage() {
        this.topic = "";
        this.payload = JsonNodeFactory.instance.objectNode();
        this.interval = 0;
        this.timeStamp = new Date();
    }
}
