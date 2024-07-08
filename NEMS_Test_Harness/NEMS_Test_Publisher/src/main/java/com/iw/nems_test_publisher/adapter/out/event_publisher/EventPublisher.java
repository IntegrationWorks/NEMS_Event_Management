package com.iw.nems_test_publisher.adapter.out.event_publisher;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iw.nems_test_publisher.adapter.out.event_publisher.util.EventUtil;
import com.iw.nems_test_publisher.application.domain.model.EventPayload;
import com.iw.nems_test_publisher.application.domain.model.TimeStampedMessage;
import com.iw.nems_test_publisher.application.port.out.GetEventPort;
import com.solace.messaging.MessagingService;
import com.solace.messaging.publisher.OutboundMessage;
import com.solace.messaging.publisher.OutboundMessageBuilder;
import com.solace.messaging.publisher.PersistentMessagePublisher;
import com.solace.messaging.resources.Topic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventPublisher implements GetEventPort {

    private final PersistentMessagePublisher publisher;
    private final MessagingService messagingService = EventUtil.ConnectBasic();

    private static final Logger logger = LogManager.getLogger();

    @Override
    public EventPayload getEventPayload(EventPayload eventPayload) {
        System.out.println("Publisher received new event payload! starting new Thread...");
        try{
            new Thread(() -> {
                SendPayloadAsMessage(eventPayload);
                System.out.println("All messages published, closing thread...");
            }).start();
        } catch(RuntimeException e){
            logger.warn("### Caught while trying to create or start new Thread",e);
        } 
        return eventPayload;
    }

    private void SendPayloadAsMessage(EventPayload inPayload) {
        OutboundMessageBuilder messageBuilder = messagingService.messageBuilder();
        for(int i = 0; i < inPayload.getPayload().length; i++) {

            // Take payload string, convert to timestamped message
            JsonNode payload = inPayload.getPayload()[i];
            TimeStampedMessage timeStampedMessage = new TimeStampedMessage(payload);

            try {
                // Map Object to json string
                ObjectMapper mapper = new ObjectMapper();
                String result = mapper.writeValueAsString(timeStampedMessage);

                // Create OutboundMessage using mapped JSON string
                OutboundMessage message = messageBuilder.build(result);

                //Logs message number out of total messages to be sent on thread
                System.out.println("sending message #" + (i+1) + "/" + inPayload.getPayload().length);

                //publish message
                publisher.publish(message, Topic.of(inPayload.getTopic()));
            } catch (RuntimeException e) {
                logger.warn("### Caught while trying to publisher.publish()",e);
            } catch (JsonProcessingException e){

            } finally {
                if(i + 1 < inPayload.getPayload().length) {
                    try {
                        // Not a big fan of this, may change this at some point
                        Thread.sleep((int)(1000 * inPayload.getInterval()));  // do Thread.sleep(0) for max speed
                        // Note: STANDARD Edition Solace PubSub+ broker is limited to 10k msg/s max ingress
                    } catch (InterruptedException e) {
                        logger.warn("### Caught while trying to run Thread.sleep()",e);
                    }
                }
            }
        }
    }
}
