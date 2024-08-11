package com.iw.nems_test_subscriber.application.domain.model;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutboxMessage {

    private int sequenceNum;
    private int subscriberId;
    private int messageId;
    private String nhi;
    private JsonNode message;
    private String status;

    public OutboxMessage(TimeStampedMessage tsMessage){
        this.nhi = tsMessage.getContent().get("nhi").asText();
        this.message = tsMessage.getContent();
        this.status = "new";
    }

}