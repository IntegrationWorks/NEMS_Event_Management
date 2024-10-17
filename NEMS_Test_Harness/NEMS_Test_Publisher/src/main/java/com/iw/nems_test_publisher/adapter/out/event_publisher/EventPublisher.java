package com.iw.nems_test_publisher.adapter.out.event_publisher;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iw.nems_test_publisher.adapter.out.event_publisher.util.EventUtil;
import com.iw.nems_test_publisher.application.domain.model.EventPayload;
import com.iw.nems_test_publisher.application.domain.model.TimeStampedMessage;
import com.iw.nems_test_publisher.application.port.out.GetEventPort;
import com.solace.messaging.MessagingService;
import com.solace.messaging.publisher.OutboundMessage;
import com.solace.messaging.publisher.OutboundMessageBuilder;
import com.solace.messaging.publisher.DirectMessagePublisher;
import com.solace.messaging.resources.Topic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventPublisher implements GetEventPort {

    private final DirectMessagePublisher publisher;
    private final MessagingService messagingService = EventUtil.ConnectBasic();

    private static final Logger logger = LogManager.getLogger();

    @Override
    public EventPayload getEventPayload(EventPayload eventPayload) {
        return getEventPayload(eventPayload, Collections.emptyMap());
    }

    @Override
    public EventPayload getEventPayload(EventPayload eventPayload, Map<String, String> headers) {
        System.out.println("Publisher received new event payload! starting new Thread...");
        try{
            new Thread(() -> {
                SendPayloadAsMessage(eventPayload, headers);
                System.out.println("All messages published, closing thread...");
            }).start();
        } catch(RuntimeException e){
            logger.warn("### Caught while trying to create or start new Thread",e);
        } 
        return eventPayload;
    }

    private void SendPayloadAsMessage(EventPayload inPayload, Map<String, String> headers) {

        OutboundMessageBuilder messageBuilder = messagingService.messageBuilder();

        Properties baseProperties = new Properties();
        baseProperties.putAll(headers);

        int msgToPubNum = 0;

        if(inPayload.getMessages().length == 0){
            msgToPubNum = inPayload.getPayload().length;
        } else {
            msgToPubNum = inPayload.getMessages().length;
        }


        for(int i = 0; i < msgToPubNum; i++) {

            // Convert payload to timestamped message
            TimeStampedMessage timeStampedMessage = ConvertPayload(inPayload, i);

            Properties messageProperties = new Properties();

            messageProperties.putAll(baseProperties);
            messageProperties.putAll(timeStampedMessage.getHeaders());

            // Clear headers defined in message, all header data should be in OutboundMessage
            timeStampedMessage.setHeaders(new HashMap<String, String>());

            try {
                // Map Object to json string
                ObjectMapper mapper = new ObjectMapper();
                String result = mapper.writeValueAsString(timeStampedMessage);

                // Create OutboundMessage using mapped JSON string
                OutboundMessage message = messageBuilder.build(result, messageProperties);

                //Logs message number out of total messages to be sent on thread
                System.out.println("sending message #" + (i+1) + "/" + msgToPubNum);

                //publish message
                publisher.publish(message, Topic.of(inPayload.getTopic()));
            } catch (RuntimeException e) {
                logger.warn("### Caught while trying to publisher.publish()",e);
            } catch (JsonProcessingException e){

            } finally {
                if(i + 1 < msgToPubNum) {
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

    private TimeStampedMessage ConvertPayload(EventPayload inPayload, int index){

        TimeStampedMessage timeStampedMessage = new TimeStampedMessage();

        if(inPayload.getMessages().length == 0){  // use payload array (Backwards compatibility)
            var payload = inPayload.getPayload()[index];
            timeStampedMessage = new TimeStampedMessage(payload);
        } else {    // Use messages array 
            var message = inPayload.getMessages()[index];
            timeStampedMessage = new TimeStampedMessage(message.getContent(), message.getHeaders());
        }

        return timeStampedMessage;
    }
}
