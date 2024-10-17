package com.iw.nems_test_publisher.adapter.out.event_publisher;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.iw.nems_test_publisher.adapter.out.event_publisher.util.EventUtil;
import com.solace.messaging.MessagingService;
import com.solace.messaging.publisher.DirectMessagePublisher;

import jakarta.annotation.PreDestroy;

@Configuration
public class PublisherConfig {

    // private static final Logger logger = LoggerFactory.getLogger(PublisherConfig.class);

    // final static Properties SOLACE_PROPERTIES = GlobalProperties.setSolaceProperties("BASIC");

    final static String QUEUE_NAME = GlobalProperties.getProperty("nems.broker.queue");
    final static String TOKEN_SERVER = GlobalProperties.getProperty("solace.auth.tokenserver");
    final static String CLIENT_ID = GlobalProperties.getProperty("solace.auth.clientid");
    final static String CLIENT_SECRET = GlobalProperties.getProperty("solace.auth.clientsecret");
    final static String SCOPE = GlobalProperties.getProperty("solace.auth.scope");
    final static String ISSUER = GlobalProperties.getProperty("solace.auth.issuer");

    final MessagingService messagingService = EventUtil.ConnectBasic();

    DirectMessagePublisher publisher;

    @Bean
    public DirectMessagePublisher directMessagePublisher() {

        // build the publisher object
        //publisher = messagingService.createPersistentMessagePublisherBuilder()
        //        .onBackPressureWait(1)
        //        .build();
        DirectMessagePublisher publisher = messagingService.createDirectMessagePublisherBuilder().onBackPressureWait(1)
                    .build();
        
        publisher.start();

        // Can be called for ACL violations.
        publisher.setPublishFailureListener(e -> {
            System.out.println("### FAILED PUBLISH "+e);
        });

        return publisher;
    }

    @PreDestroy
    public void onDestroy(){
        System.out.println("Callback Triggered - @PreDestroy");
        System.out.println("Disconnecting publisher...");
        publisher.terminate(500);
        messagingService.disconnect();
    }

}
