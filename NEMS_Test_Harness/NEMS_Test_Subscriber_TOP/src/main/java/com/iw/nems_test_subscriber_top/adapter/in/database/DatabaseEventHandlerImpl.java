package com.iw.nems_test_subscriber_top.adapter.in.database;

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
    public void onTriggerPull(long id) {
        // System.out.println("Database pull has been triggered, processing...");
        pullDatabase(id);
    }

    @Override
    public void pullDatabase(long id) {

        OutboxMessage obMessage = outboxRepository.findById(id).get();

        String messageStatus = obMessage.getStatus();

        if(messageStatus.equalsIgnoreCase("new")){
            sendMessageUseCase.processMessage(obMessage);
        } else {
            System.err.println("Message status != new");
        }

    }

}
