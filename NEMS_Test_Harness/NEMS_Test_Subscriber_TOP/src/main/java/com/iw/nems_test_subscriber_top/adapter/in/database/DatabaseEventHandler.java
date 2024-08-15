package com.iw.nems_test_subscriber_top.adapter.in.database;

public interface DatabaseEventHandler {

    void onTriggerPull(long id);

    void pullDatabase(long id);

}
