package com.example.bank.domain.events;

import com.example.bank.domain.logic.ChangingToJson;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountTransactionEvent(
        UUID transactionID,
        BigDecimal transactionAmount,
        Long accountNumber,
        String status
) implements Event {
    /**
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public String toJson() throws JsonProcessingException {
        ChangingToJson changingToJson = new ChangingToJson();
        return changingToJson.toJson(this);
    }
}
