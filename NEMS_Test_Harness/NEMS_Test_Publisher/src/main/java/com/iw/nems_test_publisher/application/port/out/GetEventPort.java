package com.iw.nems_test_publisher.application.port.out;

import java.util.Map;

import com.iw.nems_test_publisher.application.domain.model.EventPayload;

public interface GetEventPort {
    
    EventPayload getEventPayload(EventPayload eventPayload);
    EventPayload getEventPayload(EventPayload eventPayload, Map<String,String> headers);
    
}
