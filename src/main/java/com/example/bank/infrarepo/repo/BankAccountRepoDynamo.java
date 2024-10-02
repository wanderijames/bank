package com.example.bank.infrarepo.repo;

import com.example.bank.domain.logic.ModelEntityRepo;
import com.example.bank.domain.model.BankAccount;
import com.example.bank.infrarepo.entities.dynamo.Account;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.Optional;


public class BankAccountRepoDynamo implements ModelEntityRepo<BankAccount, Long> {

    private final DynamoDbEnhancedClient enhancedClient;

    public BankAccountRepoDynamo(DynamoDbClient dynamoDbClient) {
        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }


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
        DynamoDbTable<Account> accountTable = enhancedClient.table("ACCOUNT", TableSchema.fromBean(Account.class));
        accountTable.putItem(fromModel(record));
        return Optional.of(record);
    }

    /**
     * Update a bank account to the persistent layer
     * @param record a bank account that needs to be updated
     * @return a bank account
     */
    public BankAccount update(BankAccount record) {
        return null;
    }

    /**
     * Get bank account by id
     * @param id bank account identifier
     * @return a bank account
     */
    public BankAccount findById(Long id) {
        DynamoDbTable<Account> accountTable = enhancedClient.table("ACCOUNT", TableSchema.fromBean(Account.class));
        Account account = accountTable.getItem(r -> r.key(k -> k.partitionValue(id)));
        return toModel(account);
    }

    /**
     * Get a list of bank accounts
     * @return a list a bank account
     */
    public List<BankAccount> findAll() {
        return List.of();
    }
}
