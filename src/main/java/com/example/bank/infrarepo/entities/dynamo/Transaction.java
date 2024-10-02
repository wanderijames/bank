package com.example.bank.infrarepo.entities.dynamo;

import com.example.bank.infrarepo.entities.TransactionInterface;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@DynamoDbBean
public class Transaction implements TransactionInterface<Account> {

    private UUID transactionID;

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

    @DynamoDbPartitionKey
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

    @DynamoDbSortKey
    public Account getAccount() {
        return null;
    }


}

