package com.example.bank.domain.logic;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.concurrent.Callable;

public interface EventPublisher {
    void setEvent(Callable<String> callableEvent) throws Exception;
    String getEvent();
    void publish() throws JsonProcessingException;
}
