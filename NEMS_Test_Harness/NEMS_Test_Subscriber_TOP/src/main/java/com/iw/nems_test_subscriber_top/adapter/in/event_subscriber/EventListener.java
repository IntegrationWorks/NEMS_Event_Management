package com.iw.nems_test_subscriber_top.adapter.in.event_subscriber;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iw.nems_test_subscriber_top.application.domain.model.TimeStampedMessage;
import com.iw.nems_test_subscriber_top.application.port.in.OutboxManagementUseCase;
// import com.iw.nems_test_subscriber_top.application.port.in.SendMessageUseCase;
import com.solace.messaging.receiver.InboundMessage;
// import com.solace.messaging.receiver.PersistentMessageReceiver;
import com.solace.messaging.receiver.PersistentMessageReceiver;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EventListener {

    
    private final OutboxManagementUseCase outboxManagementUseCase;
    PersistentMessageReceiver receiver;

    public void processEvent(InboundMessage message){

        String jsonString = message.getPayloadAsString();

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        mapper.setDateFormat(df);

        String replicationGroupMessageId = message.getReplicationGroupMessageId().toString();

        try {
            TimeStampedMessage tsMessage = mapper.readerFor(TimeStampedMessage.class).readValue(jsonString);
            if(replicationGroupMessageId != null){
                outboxManagementUseCase.consumeMessage(tsMessage, replicationGroupMessageId);
            }

        } catch (IOException e) {
            System.err.println("New message could not be parsed back into a TimeStampedMessage object: " + e.toString());
        }

        // System.out.println(jsonString);
    }

}
