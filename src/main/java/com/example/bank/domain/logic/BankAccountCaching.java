package com.example.bank.domain.logic;

import com.example.bank.domain.model.BankAccount;

import java.util.Optional;

public interface BankAccountCaching extends DataCaching<BankAccount> {
    boolean put(BankAccount cacheObject, String cacheKey, Optional<Long> expiresInSeconds);
    boolean delete(String cacheKey);
    BankAccount get(String cacheKey);
}
