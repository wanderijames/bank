package com.example.bank.domain.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public record BankAccount(Long accountNumber, BigDecimal accountBalance, Date balanceUpdateDate) {
    /**
     * @param accountNumber account identifier
     * @param accountBalance   account balance, new accounts will always have a zero balance
     *                  if they don't have any transaction
     */
    public BankAccount {
        Objects.requireNonNull(accountNumber);
        Objects.requireNonNull(accountBalance);
    }

    /**
     * Checks whether the account can transact against the amount
     * @param transactionAmount value of the transaction
     * @return True of if the account has enough balance otherwise False
     */
    public boolean canTransact(BigDecimal transactionAmount) {
        return accountBalance.compareTo(transactionAmount) > 0;
    }

    /**
     * Conducting a transaction in the account
     * @param transactionAmount the value of the transaction
     * @param description A description of the transaction
     * @return An account transaction with a new account balance
     */
    public AccountTransaction transact(BigDecimal transactionAmount, String description) {
        AccountTransaction accountTransaction = null;
        if (canTransact(transactionAmount)) {
            final BigDecimal newBalance = accountBalance.add(transactionAmount);
            final BankAccount bankAccount = new BankAccount(accountNumber, newBalance, new Date());
            final Date transactionDate = new Date();
            accountTransaction = new AccountTransaction(
                    bankAccount, transactionAmount, description, transactionDate, newBalance
            );
        }
        return accountTransaction;
    }

    /**
     * Withdraw an amount from a bank account
     * @param transactionAmount The amount to be debited
     * @return An account transaction with a new account balance
     */
    public AccountTransaction withdraw(BigDecimal transactionAmount) {
        return transact(transactionAmount.abs().negate(), "withdraw");
    }

    /**
     * Withdraw an amount from a bank account
     * @param transactionAmount The amount to be debited
     * @param description A description of the transaction
     * @return An account transaction with a new account balance
     */
    public AccountTransaction withdraw(BigDecimal transactionAmount, String description) {
        return transact(transactionAmount.abs().negate(), description);
    }

    /**
     * Deposit an amount from a bank account
     * @param transactionAmount The amount to be credited
     * @return An account transaction with a new account balance
     */
    public AccountTransaction deposit(BigDecimal transactionAmount) {
        return transact(transactionAmount.abs(), "deposit");
    }

    /**
     * Deposit an amount from a bank account
     * @param transactionAmount The amount to be credited
     * @param description A description of the transaction
     * @return An account transaction with a new account balance
     */
    public AccountTransaction deposit(BigDecimal transactionAmount, String description) {
        return transact(transactionAmount.abs(), "deposit");
    }

}
