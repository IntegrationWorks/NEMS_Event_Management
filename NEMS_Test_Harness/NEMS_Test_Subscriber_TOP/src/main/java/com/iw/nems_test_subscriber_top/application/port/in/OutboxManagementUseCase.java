package com.iw.nems_test_subscriber_top.application.port.in;

import com.iw.nems_test_subscriber_top.application.domain.model.TimeStampedMessage;

public interface OutboxManagementUseCase {
    void consumeMessage(TimeStampedMessage tsMessage, String messageId);
}
