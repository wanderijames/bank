package com.example.bank.domain.model;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public record AccountTransaction(
        BankAccount bankAccount,
        UUID transactionID,
        BigDecimal transactionAmount,
        String description,
        Date transactionDate,
        BigDecimal accountBalance
) {

    public AccountTransaction {
        Objects.requireNonNull(bankAccount);
        Objects.requireNonNull(transactionAmount);
        Objects.requireNonNull(accountBalance);
        Objects.requireNonNull(transactionDate);
        Objects.requireNonNull(description);
    }

    public AccountTransaction(
            BankAccount bankAccount,
            BigDecimal transactionAmount,
            String description,
            Date transactionDate,
            BigDecimal accountBalance
    ) {
        this(
                bankAccount,
                generateInternalReference(bankAccount.accountNumber(), transactionAmount, "not provided", transactionDate),
                transactionAmount,
                description,
                transactionDate,
                accountBalance);
    }

    /**
     * Generate internal reference that will be used for lookups
     * and ensuring idempotency. This reference maybe use as a primary key
     * in relational databases.
     * The generated reference should be unique within a day for the same amount, account id and description.
     * Account ID, amount and transaction date will be used to generate the reference
     * @param accountNumber The bank account identifier
     * @param transactionAmount A negative amount implies a debit while a positive implies a credit
     * @param description A transaction description
     * @param transactionDate The date for the transaction
     */
    public static UUID generateInternalReference(Long accountNumber, BigDecimal transactionAmount,String description, Date transactionDate) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy");
        final String formattedDate = dateFormatter.format(transactionDate);
        return UUID.fromString(accountNumber.toString() + description + transactionAmount.toString() + formattedDate);
    }
}
