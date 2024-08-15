package com.iw.nems_test_subscriber_top.infrastructure.repositories.outbox;

import org.springframework.data.repository.CrudRepository;

import com.iw.nems_test_subscriber_top.application.domain.model.OutboxMessage;
import java.util.List;


public interface OutboxRepository extends CrudRepository<OutboxMessage, Long> {
    List<OutboxMessage> findByStatus(String status);
}
