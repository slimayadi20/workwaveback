package com.example.workwave.controllers;

import com.example.workwave.entities.BankAccount;
import com.example.workwave.entities.Transactions;
import com.example.workwave.exception.ResourceNotFoundException;
import com.example.workwave.repositories.BankAccountRepository;
import com.example.workwave.repositories.TransactionRepository;
import com.example.workwave.services.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BalanceController {

    @Autowired
    private BalanceService balanceService;
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/balance")
    public List<BalanceChange> getBalanceChangesForPastMonth(@RequestParam Long bankAccountId) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new ResourceNotFoundException("BankAccount"));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        List<Transactions> transactions = transactionRepository.findByBankAccountAndTransactionDateBetween(bankAccount, startDate, endDate);
        Double balance = bankAccount.getBalance();
        List<BalanceChange> balanceChanges = new ArrayList<>();
        Map<LocalDate, Double> balanceByDate = new HashMap<>();
        for (Transactions transaction : transactions) {
            LocalDate transactionDate = transaction.getTransactionDate();
            if (!balanceByDate.containsKey(transactionDate)) {
                balanceByDate.put(transactionDate, balance);
            }
            Double amount = transaction.getAmount();
            balance = balance - amount;
            balanceByDate.put(transactionDate, balance);
        }
        for (int i = 0; i < 30; i++) {
            LocalDate currentDate = endDate.minusDays(i);
            Double currentBalance = balanceByDate.get(currentDate);
            if (currentBalance == null) {
                currentBalance = balance;
            }
            balanceChanges.add(new BalanceChange(currentDate, currentBalance));
        }
        return balanceChanges;
    }
    public static class BalanceChange {
        private LocalDate date;
        private Double balance;

        public BalanceChange(LocalDate date, Double balance) {
            this.date = date;
            this.balance = balance;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
            this.balance = balance;
        }
    }
}