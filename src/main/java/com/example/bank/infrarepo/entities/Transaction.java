package com.example.bank.infrarepo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
public class Transaction {
    @Id
    private UUID transactionID;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private BigDecimal transactionAmount;
    private String description;
    private Date transactionDate;
    private BigDecimal accountBalance;

    public Transaction() {}

    public Transaction(UUID transactionID, Account account, BigDecimal transactionAmount, String description, Date transactionDate, BigDecimal accountBalance) {
        this.transactionID = transactionID;
        this.transactionAmount = transactionAmount;
        this.description = description;
        this.transactionDate = transactionDate;
        this.accountBalance = accountBalance;
        this.account = account;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public UUID getTransactionID() {
        return transactionID;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }
    public String getDescription() {
        return description;
    }
    public Date getTransactionDate() {
        return transactionDate;
    }
    public BigDecimal getAccountBalance() {
        return accountBalance;
    }
    public Account getAccount() {
        return account;
    }

}

