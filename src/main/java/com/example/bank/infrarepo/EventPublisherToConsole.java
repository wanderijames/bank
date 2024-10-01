package com.example.bank.infrarepo;

import com.example.bank.domain.logic.EventPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.concurrent.Callable;

@Component
public class EventPublisherToConsole implements EventPublisher {
    private String event;

    public void setEvent(Callable<String> callableEvent) throws Exception {
        event = callableEvent.call();
    }

    /**
     * Publishes to logs
     */
    public void publish() throws JsonProcessingException {
        System.out.println(getEvent());

    }

    /**
     * @return an event that can be transported as string
     */
    public String getEvent() {
        return event;
    }
}
