package com.iw.nems_test_subscriber_top.adapter.in.database;

import java.util.List;

import org.springframework.stereotype.Controller;


import com.iw.nems_test_subscriber_top.application.domain.model.OutboxMessage;
import com.iw.nems_test_subscriber_top.application.port.in.SendMessageUseCase;
import com.iw.nems_test_subscriber_top.infrastructure.repositories.outbox.OutboxRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DatabaseEventHandlerImpl implements DatabaseEventHandler {

    final private SendMessageUseCase sendMessageUseCase;
    final private OutboxRepository outboxRepository;

    @Override
    public void onTriggerPull() {
        // System.out.println("Database pull has been triggered, processing...");
        pullDatabase();
    }

    @Override
    public void pullDatabase() {
        List<OutboxMessage> obMessages = outboxRepository.findByStatus("new");
        sendMessageUseCase.processNewOutboxMessages(obMessages);
    }

}
