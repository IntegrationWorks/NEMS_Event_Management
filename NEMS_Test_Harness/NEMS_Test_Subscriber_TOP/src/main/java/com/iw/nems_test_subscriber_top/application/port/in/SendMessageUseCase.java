package com.iw.nems_test_subscriber_top.application.port.in;

import java.util.List;

import com.iw.nems_test_subscriber_top.application.domain.model.OutboxMessage;
// import com.iw.nems_test_subscriber_top.application.domain.model.EventPayload;
import com.iw.nems_test_subscriber_top.application.domain.model.TimeStampedMessage;

public interface SendMessageUseCase {
    void processNewOutboxMessages(List<OutboxMessage> obMessages);
    boolean sendEventPayload(TimeStampedMessage timeStampedMessage);
    void processMessage(OutboxMessage obMessage);
}
