package com.example.bank.infrarepo.repo;

import com.example.bank.domain.logic.ModelEntityRepo;
import com.example.bank.domain.model.AccountTransaction;
import com.example.bank.domain.model.BankAccount;
import com.example.bank.infrarepo.entities.jpa.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import com.example.bank.infrarepo.entities.jpa.Transaction;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface AccountTransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionID(UUID transactionID);
}

@Component
public class AccountTransactionRepo implements ModelEntityRepo<AccountTransaction, UUID> {

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;

    public Transaction fromModel(AccountTransaction accountTransaction) {
        BankAccountRepo accountRepo = new BankAccountRepo();
        Account account = accountRepo.fromModel(accountTransaction.bankAccount());
        account.setAccountNumber(accountTransaction.bankAccount().accountNumber());
        return new Transaction(
                accountTransaction.transactionID(),
                account,
                accountTransaction.transactionAmount(),
                accountTransaction.description(),
                accountTransaction.transactionDate(),
                accountTransaction.accountBalance()

        );
    }

    public AccountTransaction toModel(Transaction transaction) {
        BankAccountRepo accountRepo = new BankAccountRepo();
        BankAccount bankAccount = accountRepo.toModel(transaction.getAccount());
        return new AccountTransaction(
                bankAccount,
                transaction.getTransactionID(),
                transaction.getTransactionAmount(),
                transaction.getDescription(),
                transaction.getTransactionDate(),
                transaction.getAccountBalance()
        );
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Optional<AccountTransaction> save(AccountTransaction record) {
        Transaction savedTransaction = accountTransactionRepository.save(fromModel(record));
        return Optional.of(toModel(savedTransaction));
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public AccountTransaction update(AccountTransaction record) {
        return null;
    }


    public AccountTransaction findById(UUID id) {
        Optional<Transaction> transaction = accountTransactionRepository.findByTransactionID(id);
        return transaction.map(this::toModel).orElse(null);
    }

    public List<AccountTransaction> findAll() {
        return List.of();
    }
}
