package com.example.bank.infrarepo.entities.dynamo;

import com.example.bank.infrarepo.entities.AccountInterface;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@DynamoDbBean
public class Account implements AccountInterface {
    private Long accountNumber;
    private BigDecimal accountBalance;
    private Date balanceUpdateDate;

    private List<Transaction> transactions;


    public Account() {}

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setBalanceUpdateDate(Date balanceUpdateDate) {
        this.balanceUpdateDate = balanceUpdateDate;
    }

    @DynamoDbPartitionKey
    public Long getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public Date getBalanceUpdateDate() {
        return balanceUpdateDate;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
