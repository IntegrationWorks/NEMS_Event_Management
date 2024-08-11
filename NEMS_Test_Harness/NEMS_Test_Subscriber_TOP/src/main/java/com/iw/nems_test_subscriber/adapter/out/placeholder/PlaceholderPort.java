package com.iw.nems_test_subscriber.adapter.out.placeholder;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.iw.nems_test_subscriber.application.domain.model.TimeStampedMessage;
import com.iw.nems_test_subscriber.application.port.out.GetMessagePort;

@Component
public class PlaceholderPort implements GetMessagePort {

    @Override
    public TimeStampedMessage getEventPayload(TimeStampedMessage timeStampedMessage) {
        // TODO Auto-generated method stub
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        System.out.println("A message was received @ " + df.format(timeStampedMessage.getTimeStamp()));
        System.out.println("Content: " + timeStampedMessage.getContent());
        return timeStampedMessage;
    }

}
