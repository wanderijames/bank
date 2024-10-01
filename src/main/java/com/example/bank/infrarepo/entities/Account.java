package com.example.bank.infrarepo.entities;

import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accountNumber;
    private BigDecimal accountBalance;
    private Date balanceUpdateDate;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
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
