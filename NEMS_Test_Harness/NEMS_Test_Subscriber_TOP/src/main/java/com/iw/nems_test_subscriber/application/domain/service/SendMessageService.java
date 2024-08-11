package com.iw.nems_test_subscriber.application.domain.service;

import com.iw.nems_test_subscriber.application.domain.model.TimeStampedMessage;
//import com.iw.nems_test_subscriber.application.domain.model.EventPayload;
import com.iw.nems_test_subscriber.application.port.in.SendMessageUseCase;
import com.iw.nems_test_subscriber.application.port.out.GetMessagePort;
import com.iw.nems_test_subscriber.infrastructure.UseCase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
public class SendMessageService implements SendMessageUseCase{

    private final GetMessagePort getEventPort;

    @Override
    public void SendEventPayload(TimeStampedMessage timeStampedMessage) {
        getEventPort.getEventPayload(timeStampedMessage);
    }

}
