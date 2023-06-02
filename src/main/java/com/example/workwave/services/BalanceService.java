package com.example.workwave.services;

import com.example.workwave.entities.BankAccount;
import com.example.workwave.entities.Transactions;
import com.example.workwave.repositories.BankAccountRepository;
import com.example.workwave.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class BalanceService {

    @Autowired
    TransactionRepository transactionsRepository;
    @Autowired
    BankAccountRepository bankAccountRepository;

    public List<BalanceChange> getBalanceChange(Long accountId) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Bank Account not found"));

        List<Transactions> transactions = transactionsRepository.findByBankAccount(bankAccount);
        List<BalanceChange> balanceChanges = new ArrayList<>();

        Double currentBalance = bankAccount.getBalance();
        balanceChanges.add(new BalanceChange(LocalDate.now(), currentBalance, 0.0));

        for (int i = 1; i < 30; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            Double balance = null;
            Double percentageChange = null;

            // Calculate the balance by summing up all the transaction amounts up to the current date
            if (!transactions.isEmpty()) {
                Double tempBalance = transactions.get(0).getBankAccount().getBalance();
                for (Transactions transaction : transactions) {
                    if (transaction.getTransactionDate().isAfter(date)) {
                        break;
                    }
                    if (transaction.getAmount() != null) {
                        tempBalance -= transaction.getAmount();
                    }
                }
                balance = tempBalance;
            }

            // If no transaction with a non-null amount is found, use the previous balance
            if (balance == null) {
                balance = balanceChanges.get(balanceChanges.size() - 1).getBalance();
            }

            // Calculate percentage change
            if (currentBalance.equals(balance)) {
                percentageChange = 0.0;
            } else {
                percentageChange = (balance - currentBalance) / currentBalance * 100;
            }

            balanceChanges.add(new BalanceChange(date, balance, percentageChange));
        }

        // Sort the balance changes by date in descending order
        balanceChanges.sort(Comparator.comparing(BalanceChange::getDate).reversed());

        return balanceChanges;
    }


    public static class BalanceChange {
        private LocalDate date;
        private Double balance;
        private Double percentageChange;

        public BalanceChange(LocalDate date, Double balance, Double percentageChange) {
            this.date = date;
            this.balance = balance;
            this.percentageChange = percentageChange;
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

        public Double getPercentageChange() {
            return percentageChange;
        }

        public void setPercentageChange(Double percentageChange) {
            this.percentageChange = percentageChange;
        }
    }
}