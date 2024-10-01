package com.example.bank.domain.logic;

import com.example.bank.domain.model.BankAccount;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

public class OpeningOfBankAccount {
    private final ModelEntityRepo<BankAccount, Long> bankAccountRepo;
    private final EventPublisher eventPublisher;

    public OpeningOfBankAccount(ModelEntityRepo<BankAccount, Long> bankAccountRepo, EventPublisher eventPublisher) {
        this.bankAccountRepo = bankAccountRepo;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Generates an account number to be used for opening an account
     * @return a new account
     */
    private Long generateNewAccountNumber() {
        // We may use country, national identifier to create idempotent account numbers
        final long currentTimeMillis = System.currentTimeMillis();
        final Random randomNumber = new Random();
        final int randomNumberInt = randomNumber.nextInt(1000000);
        return Long.valueOf(String.valueOf(currentTimeMillis) + randomNumberInt);
    }

    public Optional<BankAccount> openANewBankAccount(BigDecimal accountBalance) {
        final BankAccount newBankAccount = new BankAccount(generateNewAccountNumber(), accountBalance, new Date());
        return bankAccountRepo.save(newBankAccount);
    }
}
