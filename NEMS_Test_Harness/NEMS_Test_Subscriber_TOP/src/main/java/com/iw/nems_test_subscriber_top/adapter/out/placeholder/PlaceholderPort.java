package com.iw.nems_test_subscriber_top.adapter.out.placeholder;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.iw.nems_test_subscriber_top.application.domain.model.TimeStampedMessage;
import com.iw.nems_test_subscriber_top.application.port.out.GetMessagePort;

@Component
public class PlaceholderPort implements GetMessagePort {



    @Override
    public TimeStampedMessage getEventPayload(TimeStampedMessage timeStampedMessage) {

        String messageNhi = timeStampedMessage.getContent().get("nhi").asText();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        
        System.out.println("A message was received @ " + df.format(timeStampedMessage.getTimeStamp()));
        System.out.println("Content: " + timeStampedMessage.getContent());

        if(MockNHIParser.isValid(messageNhi)){
            System.out.println("The NHI number has been validated and the message will be passed on (Mock) and deleted from outbox repository");
            return timeStampedMessage;
        } else {
            System.out.println("The NHI number has been deemed invalid. This can be because it is either formatted incorrectly or is not present");
            System.out.println("The message will now be deemed as failed");
            return null;
        }
    }

}
