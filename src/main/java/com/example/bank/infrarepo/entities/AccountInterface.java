package com.example.bank.infrarepo.entities;

import java.math.BigDecimal;
import java.util.Date;

public interface AccountInterface {

    void setAccountNumber(Long accountNumber);

    void setAccountBalance(BigDecimal accountBalance);

    void setBalanceUpdateDate(Date balanceUpdateDate);

    public Long getAccountNumber();

    public BigDecimal getAccountBalance();

    public Date getBalanceUpdateDate();
}
