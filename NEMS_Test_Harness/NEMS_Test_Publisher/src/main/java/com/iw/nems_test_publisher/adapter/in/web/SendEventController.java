package com.iw.nems_test_publisher.adapter.in.web;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.iw.nems_test_publisher.application.domain.model.EventPayload;
import com.iw.nems_test_publisher.application.port.in.SendEventUseCase;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequiredArgsConstructor
class SendPersonController {

    private final SendEventUseCase sendEventUseCase;
    private final String LS = System.lineSeparator();

    @PostMapping("/")
    ResponseEntity<String> sendPerson(@RequestHeader Map<String, String> headers, @Valid @RequestBody EventPayload eventPayload){

        //detect if it either has no messages or an empty payload (Backwards compatibility)
        if(eventPayload.getMessages().length == 0 && eventPayload.getPayload().length == 0){
            return new ResponseEntity<String>("Invalid Request Body: messages to be published must be defined in body", HttpStatus.BAD_REQUEST);
        }

        //fetch request headers, add those starting with "sol-" to map
        Map<String,String> requestHeaders = new HashMap<>();

        headers.forEach((key, value) -> {
            if(key.startsWith("sol-"))
            {
                String newKey = key.replaceFirst("sol-","");

                requestHeaders.put(newKey, value);
            }
        });

        // Send Payload and request headers to be processed for publishing
        sendEventUseCase.SendEventPayload(eventPayload, requestHeaders);

        Object[] responseArray = {};

        if(eventPayload.getMessages().length == 0){
            responseArray = eventPayload.getPayload();
        } else {
            responseArray = eventPayload.getMessages();
        }

        return new ResponseEntity<String>("new Message(s) received:" + LS
                + "######" + LS
                + Arrays.toString(responseArray) + LS
                + "######" + LS
                + "to send to topic: " + LS
                +  eventPayload.getTopic() + LS
                + "with interval: " + LS
                + eventPayload.getInterval() + LS
                + "they are being sent on an asynchronous thread and will be accessible shortly."

                , HttpStatus.OK);
    }

}
