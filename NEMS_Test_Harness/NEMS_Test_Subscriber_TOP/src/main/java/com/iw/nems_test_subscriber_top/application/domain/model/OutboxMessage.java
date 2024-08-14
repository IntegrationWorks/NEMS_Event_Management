package com.iw.nems_test_subscriber_top.application.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "outbox_table")
@NoArgsConstructor
@AllArgsConstructor
public class OutboxMessage {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long sequenceNum;

    private String subscriberId;
    private String messageId;
    
    private String nhi;
    private TimeStampedMessage message;
    private String status;

    public OutboxMessage(TimeStampedMessage tsMessage){
        this.nhi = tsMessage.getContent().get("nhi").asText();
        this.message = tsMessage;
        this.status = "new";
    }

    public OutboxMessage(TimeStampedMessage tsMessage, String messageId){
        this.nhi = tsMessage.getContent().get("nhi").asText();
        this.message = tsMessage;
        this.status = "new";
        this.messageId = messageId;
    }

}