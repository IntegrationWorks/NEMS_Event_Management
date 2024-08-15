package com.iw.nems_test_subscriber_top.application.port.out;

import com.iw.nems_test_subscriber_top.application.domain.model.OutboxMessage;

public interface DatabasePort {

    OutboxMessage insertMessage(OutboxMessage obMessage);

    OutboxMessage failMessage(long id);

    void deleteMessage(long id);

}
