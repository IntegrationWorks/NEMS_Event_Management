package com.iw.nems_test_publisher.application.domain.service;

import com.iw.nems_test_publisher.application.domain.model.EventPayload;
import com.iw.nems_test_publisher.application.port.in.SendEventUseCase;
import com.iw.nems_test_publisher.application.port.out.GetEventPort;
import com.iw.nems_test_publisher.infrastructure.UseCase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
public class SendEventService implements SendEventUseCase{

    private final GetEventPort getEventPort;

    @Override
    public void SendEventPayload(EventPayload eventPayload) {
        getEventPort.getEventPayload(eventPayload);
    }

    
    
}
