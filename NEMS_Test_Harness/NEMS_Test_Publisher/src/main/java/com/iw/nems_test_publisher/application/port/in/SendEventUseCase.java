package com.iw.nems_test_publisher.application.port.in;

import java.util.Map;

import com.iw.nems_test_publisher.application.domain.model.EventPayload;

public interface SendEventUseCase {
    void SendEventPayload(EventPayload eventPayload);
    void SendEventPayload(EventPayload eventPayload, Map<String, String> headers);
}
