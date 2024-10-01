package com.example.bank.domain.events;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface Event {

    public String toJson()  throws JsonProcessingException;
}
