package com.iw.nems_test_subscriber.adapter.in.event_subscriber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.iw.nems_test_subscriber.adapter.in.event_subscriber.util.EventUtil;
import com.solace.messaging.MessagingService;
import com.solace.messaging.receiver.PersistentMessageReceiver;
import com.solace.messaging.resources.Queue;


@Configuration
public class ReceiverConfig {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private EventListener eventListener;

    // final static Properties SOLACE_PROPERTIES = GlobalProperties.setSolaceProperties("BASIC");

    final static String QUEUE_NAME = GlobalProperties.getEnvProperty("nems.broker.queue");
    final static String TOKEN_SERVER = GlobalProperties.getEnvProperty("solace.auth.tokenserver");
    final static String CLIENT_ID = GlobalProperties.getEnvProperty("solace.auth.clientid");
    final static String CLIENT_SECRET = GlobalProperties.getEnvProperty("solace.auth.clientsecret");
    final static String SCOPE = GlobalProperties.getEnvProperty("solace.auth.scope");
    final static String ISSUER = GlobalProperties.getEnvProperty("solace.auth.issuer");

    final MessagingService messagingService = EventUtil.ConnectBasic();

    PersistentMessageReceiver receiver;

    @Bean
    public PersistentMessageReceiver persistentMessageReceiver() {

        final MessagingService messagingService = EventUtil.ConnectBasic();

        receiver = messagingService
                .createPersistentMessageReceiverBuilder()
                .build(Queue.durableExclusiveQueue(QUEUE_NAME));

        try {
            receiver.start();
            System.out.printf("Successfully connected to queue '%s'\n", QUEUE_NAME);

        } catch (RuntimeException e) {

            System.err.printf("%n*** Could not establish a connection to queue '%s': %s%n", QUEUE_NAME, e.getMessage());
            System.err.println("  or see the SEMP CURL scripts inside the 'semp-rest-api' directory.");
            System.err.println(
                    "NOTE: see HowToEnableAutoCreationOfMissingResourcesOnBroker.java sample for how to construct queue with consumer app.");
            System.err.println("Exiting.");
            
            int returnCode = 0;

            SpringApplication.exit(appContext, () -> returnCode);
            return null;
        }
        // asynchronous anonymous receiver message callback
        receiver.receiveAsync(message -> {

            // Where customer code can be implemeted to handle events before they are ACKed
            eventListener.processEvent(message);
            // Messages are removed from the broker queue when the ACK is received.
            // Therefore, DO NOT ACK until all processing/storing of this message is
            // complete.
            // NOTE that messages can be acknowledged from any thread.
            receiver.ack(message); // ACKs are asynchronous

        });

        return receiver;
    }
}
