package com.example.bank.infrarepo;

import com.example.bank.domain.logic.EventPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.concurrent.Callable;

public abstract class EventPublisherToSNS implements EventPublisher {
    private final SnsClient snsClient = SnsClient.builder()
            .region(Region.EU_WEST_1) // Specify your region
            .build();
    private String event;

    public void setEvent(Callable<String> callableEvent) throws Exception {
        event = callableEvent.call();
    }

    /**
     * Publishes to an SNS topic
     */
    public void publish() throws JsonProcessingException {
        String snsTopicArn = "arn:aws:sns:YOUR_REGION:YOUR_ACCOUNT_ID:YOUR_TOPIC_NAME";
        PublishRequest publishRequest = PublishRequest.builder()
                .message(getEvent())
                .topicArn(snsTopicArn)
                .build();
        PublishResponse publishResponse = snsClient.publish(publishRequest);
        // Handles failures and retries in publishing this
        // Check response and confirm successful publishing

    }

    /**
     * @return an event that can be transported as string
     */
    public String getEvent() {
        return event;
    }
}
