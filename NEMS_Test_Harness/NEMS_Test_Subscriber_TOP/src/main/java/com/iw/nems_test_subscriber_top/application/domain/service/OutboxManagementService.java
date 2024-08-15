package com.iw.nems_test_subscriber_top.application.domain.service;

import org.springframework.core.env.Environment;

import com.iw.nems_test_subscriber_top.application.domain.model.OutboxMessage;
import com.iw.nems_test_subscriber_top.application.domain.model.TimeStampedMessage;
import com.iw.nems_test_subscriber_top.application.port.in.OutboxManagementUseCase;
import com.iw.nems_test_subscriber_top.application.port.out.DatabasePort;
import com.iw.nems_test_subscriber_top.infrastructure.UseCase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
public class OutboxManagementService implements OutboxManagementUseCase{

    private final DatabasePort dbPort;
    private final Environment env;

    @Override
    public long writeMessageToDb(TimeStampedMessage tsMessage, String messageId) {
        OutboxMessage obMessage = new OutboxMessage(tsMessage, messageId);
        obMessage.setSubscriberId(env.getProperty("subscriber.app.id"));
        OutboxMessage obMessageEntry = dbPort.insertMessage(obMessage);
        return obMessageEntry.getSequenceNum();
    }

}
