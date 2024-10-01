package com.example.bank.domain.events;

import com.example.bank.domain.model.AccountTransaction;
import com.example.bank.domain.logic.ChangingToJson;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Date;

public record WithdrawalEvent (
        AccountTransaction transaction,
        String status,
        Date eventTime
) implements Event {
    /**
     * @return a json string representation
     * @throws JsonProcessingException if an object cannot be converted to json
     */
    public String toJson() throws JsonProcessingException {
        ChangingToJson changingToJson = new ChangingToJson();
        return changingToJson.toJson(this);
    }
}
