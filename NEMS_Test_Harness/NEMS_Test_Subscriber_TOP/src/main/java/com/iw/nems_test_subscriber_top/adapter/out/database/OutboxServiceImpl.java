package com.iw.nems_test_subscriber_top.adapter.out.database;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.iw.nems_test_subscriber_top.application.domain.model.OutboxMessage;
import com.iw.nems_test_subscriber_top.application.exception.EntityNotFoundException;
import com.iw.nems_test_subscriber_top.infrastructure.repositories.outbox.OutboxRepository;

import lombok.AllArgsConstructor;

@Service(value = "outboxService")
@AllArgsConstructor
public class OutboxServiceImpl implements OutboxService{

    private OutboxRepository outboxRepository;

    @Override
    public OutboxMessage insertMessage(OutboxMessage obMessage) {
        return outboxRepository.save(obMessage);
    }

    @Override
    public void deleteMessage(long id) {
        Optional<OutboxMessage> obMessage = outboxRepository.findById(id);
        OutboxMessage unwrappedMessage = unwrapMessage(obMessage, id);
        outboxRepository.delete(unwrappedMessage);
    }

    @Override
    public OutboxMessage failMessage(long id) {
        Optional<OutboxMessage> obMessage = outboxRepository.findById(id);
        OutboxMessage unwrappedMessage = unwrapMessage(obMessage, id);
        unwrappedMessage.setStatus("failed");
        return outboxRepository.save(unwrappedMessage);
    }

    
    static OutboxMessage unwrapMessage(Optional<OutboxMessage> entity, Long id){
        if (entity.isPresent()) return entity.get();
        else throw new EntityNotFoundException(id, OutboxMessage.class);
    }

}
