package com.iw.nems_test_publisher.application.port.in;

import com.iw.nems_test_publisher.application.domain.model.EventPayload;

public interface SendEventUseCase {
    void SendEventPayload(EventPayload eventPayload);
}
