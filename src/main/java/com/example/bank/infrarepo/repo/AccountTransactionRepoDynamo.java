package com.example.bank.infrarepo.repo;

import com.example.bank.domain.logic.ModelEntityRepo;
import com.example.bank.domain.model.AccountTransaction;
import com.example.bank.domain.model.BankAccount;
import com.example.bank.infrarepo.entities.dynamo.Transaction;
import com.example.bank.infrarepo.entities.dynamo.Account;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountTransactionRepoDynamo implements ModelEntityRepo<AccountTransaction, UUID> {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbClient dynamoDbClient;

    public AccountTransactionRepoDynamo(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    public Transaction fromModel(AccountTransaction accountTransaction) {
        BankAccountRepoDynamo accountRepo = new BankAccountRepoDynamo(this.dynamoDbClient);
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
        BankAccountRepoDynamo accountRepo = new BankAccountRepoDynamo(this.dynamoDbClient);
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

    public Optional<AccountTransaction> save(AccountTransaction record) {
        DynamoDbTable<Transaction> transactionTable = enhancedClient.table("TRANSACTION", TableSchema.fromBean(Transaction.class));
        Put putTransaction = Put.builder()
                        .tableName("TRANSACTION")
                        .item(transactionTable.tableSchema().itemToMap(fromModel(record), true))
                        .build();
        TransactWriteItemsRequest transactWriteItemsRequest = TransactWriteItemsRequest.builder()
                .transactItems(
                        TransactWriteItem.builder().put(putTransaction).build()
                )
                .build();
        try {
            dynamoDbClient.transactWriteItems(transactWriteItemsRequest);
            return Optional.of(record);
        } catch (TransactionCanceledException e) {
            // Handle transaction cancellation
        } catch (ResourceNotFoundException e) {
            // Handle resource not found
        }
        return Optional.empty();
    }

    public AccountTransaction update(AccountTransaction record) {
        return null;
    }

    public AccountTransaction findById(UUID id) {
        return null;
    }

    public List findAll() {
        return List.of();
    }
}
