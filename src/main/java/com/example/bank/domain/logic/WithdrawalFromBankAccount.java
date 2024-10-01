package com.example.bank.domain.logic;

import com.example.bank.domain.events.WithdrawalEvent;
import com.example.bank.domain.model.AccountTransaction;
import com.example.bank.domain.model.BankAccount;

import java.math.BigDecimal;
import java.util.*;

public class WithdrawalFromBankAccount {

    private final ModelEntityRepo<BankAccount, Long> bankAccountRepo;
    private final ModelEntityRepo<AccountTransaction, UUID> accountTransactionRepo;
    private final EventPublisher eventPublisher;
    private final AccountTransactionCaching accountTransactionCaching;

    public WithdrawalFromBankAccount(
            ModelEntityRepo<BankAccount, Long> bankAccountRepo,
            ModelEntityRepo<AccountTransaction, UUID> accountTransactionRepo,
            EventPublisher eventPublisher,
            AccountTransactionCaching accountTransactionCaching
    ) {
        this.bankAccountRepo = bankAccountRepo;
        this.accountTransactionRepo = accountTransactionRepo;
        this.eventPublisher = eventPublisher;
        this.accountTransactionCaching = accountTransactionCaching;
    }

    /**
     * This helps us to check if a transaction has happened
     * @param accountTransaction a transaction to lookup
     * @return an account transaction
     */
    private Optional<AccountTransaction> getAccountTransaction(AccountTransaction accountTransaction) {
        AccountTransaction accountTransactionToReturnFromCache = accountTransactionCaching.get(accountTransaction.transactionID().toString());
        if (accountTransactionToReturnFromCache != null) {
            return Optional.of(accountTransactionToReturnFromCache);
        }
        AccountTransaction accountTransactionToReturnFromRepo = accountTransactionRepo.findById(accountTransaction.transactionID());
        if (accountTransactionToReturnFromRepo != null) {
            return Optional.of(accountTransactionToReturnFromRepo);
        }
        return Optional.empty();
    }

    /**
     * This helps us to add the recent transaction to our cache to avoid duplicity
     * Nonetheless, the transaction primary key is generated using a hash od transaction paramters
     *
     * @param accountTransaction a transaction to persist in our cache
     */
    private void cacheTransaction(AccountTransaction accountTransaction) {
        accountTransactionCaching.put(accountTransaction, accountTransaction.transactionID().toString(), 86400);
    }

    public String withdraw(Long accountNumber, BigDecimal amount) throws Exception {
        BankAccount bankAccount = bankAccountRepo.findById(accountNumber);
        if (bankAccount == null) {
            return "Withdrawal failed: Unknown account number: " + accountNumber;
        }
        final AccountTransaction accountTransaction = bankAccount.withdraw(amount);
        // check if we already did the transaction earlier
        final Optional<AccountTransaction> similarAccountTransaction = this.getAccountTransaction(accountTransaction);
        if (similarAccountTransaction.isPresent()) {
            return "Withdrawal successful";
        }
        if (bankAccount.canTransact(amount)) {
            Optional<AccountTransaction> savedAccountTransaction = accountTransactionRepo.save(accountTransaction);
            if (savedAccountTransaction.isPresent()) {
                this.cacheTransaction(savedAccountTransaction.get());
                // This is just to demonstrate that we can also use vector clocks
                // for events resolutions to downstream applications
                Map<String, Long> clock = new HashMap<>();
                clock.put("WITHDRAWAL_SERVCE", System.currentTimeMillis());
                VectorClocks vectorClocks = new VectorClocks(clock);

                WithdrawalEvent withdrawalEvent = new WithdrawalEvent(
                        accountTransaction,
                        "SUCCESSFUL",
                        new Date(),
                        vectorClocks
                );
                eventPublisher.setEvent(withdrawalEvent::toJson);
                eventPublisher.publish();
                return "Withdrawal successful";
            }
        } else {
            return "Insufficient funds for withdrawal";
        }
        return "Withdrawal failed";
    }
}
