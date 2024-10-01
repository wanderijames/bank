package com.example.bank.web;

import com.example.bank.domain.logic.ChangingToJson;
import com.example.bank.domain.logic.OpeningOfBankAccount;
import com.example.bank.domain.logic.VectorClocks;
import com.example.bank.domain.logic.WithdrawalFromBankAccount;
import com.example.bank.domain.model.BankAccount;
import com.example.bank.infrarepo.AccountTransactionCachingSimple;
import com.example.bank.infrarepo.EventPublisherToConsole;
import com.example.bank.infrarepo.repo.BankAccountRepo;
import com.example.bank.infrarepo.repo.AccountTransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@EnableCaching
@RestController
@RequestMapping("/bank")
public class BankAccountController {

    @Autowired
    private BankAccountRepo bankAccountRepo;

    @Autowired
    private AccountTransactionRepo accountTransactionRepo;

    @Autowired
    private EventPublisherToConsole eventPublisherToConsole;

    @Autowired
    private AccountTransactionCachingSimple accountTransactionCachingSimple;

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("accountId") Long accountId,
                           @RequestParam("amount") BigDecimal amount) throws Exception {
        WithdrawalFromBankAccount withdrawalFromBankAccount = new WithdrawalFromBankAccount(
                bankAccountRepo, accountTransactionRepo, eventPublisherToConsole, accountTransactionCachingSimple
        );
        return withdrawalFromBankAccount.withdraw(accountId, amount);
    }

    @PostMapping(value = "/openAccount")
    public String withdraw(@RequestParam("openingBalance") BigDecimal openingBalance) throws Exception {
        OpeningOfBankAccount openingOfBankAccount = new OpeningOfBankAccount(bankAccountRepo, eventPublisherToConsole);
        Optional<BankAccount> bankAccount = openingOfBankAccount.openANewBankAccount(openingBalance);
        if (bankAccount.isPresent()) {
            ChangingToJson changingToJson = new ChangingToJson();
            return changingToJson.toJson(bankAccount.get());
        }
        return "Unable to open an account";
    }
}
