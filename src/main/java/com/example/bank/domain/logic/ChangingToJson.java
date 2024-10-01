package com.example.bank.domain.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ChangingToJson {

    public String toJson(Object object) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(object);
    }
}
