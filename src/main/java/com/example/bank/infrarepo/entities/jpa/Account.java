package com.example.bank.infrarepo.entities.jpa;

import com.example.bank.infrarepo.entities.AccountInterface;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
public class Account implements AccountInterface {
    @Id
    private Long accountNumber;
    private BigDecimal accountBalance;
    private Date balanceUpdateDate;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
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
