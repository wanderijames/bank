package com.example.bank.infrarepo.entities;


import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public interface TransactionInterface<A> {

    void setAccountBalance(BigDecimal accountBalance);

    void setAccount(A account);

    UUID getTransactionID();

    BigDecimal getTransactionAmount();

    String getDescription();

    Date getTransactionDate();

    BigDecimal getAccountBalance();

    A getAccount();
}
