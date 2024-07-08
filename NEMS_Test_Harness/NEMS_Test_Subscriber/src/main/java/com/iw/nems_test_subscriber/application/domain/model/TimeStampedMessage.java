package com.iw.nems_test_subscriber.application.domain.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeStampedMessage implements Serializable {

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    private Date timeStamp;

    public TimeStampedMessage(String message) {
        this.content = message;
        this.timeStamp = new Date();
    }

    public TimeStampedMessage() {
        this.content = "";
        this.timeStamp = new Date();
    }
}
