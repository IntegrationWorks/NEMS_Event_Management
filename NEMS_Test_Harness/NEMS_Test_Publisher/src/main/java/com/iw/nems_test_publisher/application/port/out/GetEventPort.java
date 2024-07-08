package com.iw.nems_test_publisher.application.port.out;

import com.iw.nems_test_publisher.application.domain.model.EventPayload;

public interface GetEventPort {
    
    EventPayload getEventPayload(EventPayload eventPayload);
    
}
