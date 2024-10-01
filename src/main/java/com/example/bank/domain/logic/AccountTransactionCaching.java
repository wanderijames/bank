package com.example.bank.domain.logic;

import com.example.bank.domain.model.AccountTransaction;

import java.util.Optional;

public interface AccountTransactionCaching extends DataCaching<AccountTransaction> {
    boolean put(AccountTransaction cacheObject, String cacheKey, int expiresInSeconds);
    boolean delete(String cacheKey);
    AccountTransaction get(String cacheKey);
}
