package com.iw.nems_test_subscriber_top.application.port.in;

import com.iw.nems_test_subscriber_top.application.domain.model.TimeStampedMessage;

public interface OutboxManagementUseCase {
    long writeMessageToDb(TimeStampedMessage tsMessage, String messageId);
}
