package com.example.bank.infrarepo.repo;

import com.example.bank.domain.logic.ModelEntityRepo;
import com.example.bank.domain.model.BankAccount;
import com.example.bank.infrarepo.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(Long accountNumber);
}

@Component
public class BankAccountRepo implements ModelEntityRepo<BankAccount, Long> {

    @Autowired
    private AccountRepository accountRepository;


    public Account fromModel(BankAccount bankAccount) {
        Account account = new Account();
        account.setAccountNumber(bankAccount.accountNumber());
        account.setAccountBalance(bankAccount.accountBalance());
        account.setBalanceUpdateDate(bankAccount.balanceUpdateDate());
        return account;
    }

    public BankAccount toModel(Account account) {
        return new BankAccount(
               account.getAccountNumber(),
                account.getAccountBalance(),
                account.getBalanceUpdateDate()
        );
    }

    /**
     * Save an account to the persistence layer
     *
     * @param record a bank account record to be persisted
     * @return a saved bank account
     */
    public Optional<BankAccount> save(BankAccount record) {
        accountRepository.save(fromModel(record));
        return Optional.of(record);
    }

    /**
     * Update a bank account to the persistent layer
     * @param record a bank account that needs to be updated
     * @return a bank account
     */
    @Override
    public BankAccount update(BankAccount record) {
        return null;
    }

    /**
     * Get bank account by id
     * @param id bank account identifier
     * @return a bank account
     */
    @Override
    public BankAccount findById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.map(this::toModel).orElse(null);
    }

    /**
     * Get a list of bank accounts
     * @return a list a bank account
     */
    @Override
    public List<BankAccount> findAll() {
        return List.of();
    }
}
