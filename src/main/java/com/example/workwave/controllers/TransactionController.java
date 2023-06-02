package com.example.workwave.controllers;

import com.example.workwave.entities.*;

import com.example.workwave.repositories.*;
import com.example.workwave.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    TransactionServiceImpl transactionService;



    @PostMapping("/addTransaction")
    public String addTransaction(Transactions transaction) {
        BankAccount bankAccount = bankAccountRepository.findById(transaction.getBankAccount().getId())
                .orElseThrow(() -> new RuntimeException("Bank account not found"));

        double newBalance = bankAccount.getBalance() + transaction.getAmount();

        if (newBalance > bankAccount.getLimitAmount()) {
            newBalance = bankAccount.getLimitAmount();
        }

        bankAccount.setBalance(newBalance);
        bankAccountRepository.save(bankAccount);

        transactionRepository.save(transaction);

        return "Transaction added successfully";
    }


    @DeleteMapping("/deleteTransaction/{id}")
    public String deleteTransaction(@PathVariable long id) {
        return transactionService.deleteTransaction(id);
    }

    @PutMapping("/updateTransaction")
    public Transactions updateTransaction(@RequestBody Transactions transactions) {
        return transactionService.updateTransaction(transactions);
    }

    @GetMapping("/Transactions")//affichage+pagination
    public List<Transactions> show() {
        return transactionService.GetAllTransactions();
    }

    @GetMapping("/Transaction/{id}")
    public Transactions getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }
    @GetMapping("/TransactionsByBankAccount/{id}")
    public List<Transactions> getTransactionByBankAccount(@PathVariable long id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank Account not found"));

        List<Transactions> transactions = transactionRepository.findByBankAccount(bankAccount);
        if (transactions == null) {
            throw new RuntimeException("Transaction not found for Bank Account");
        }
        return transactions;
    }
    // ***************** STATISTICSSS ********************
    @GetMapping("/{bankAccountId}/transactions-count-by-month")
    public ResponseEntity<Long> getTransactionsCountByMonthAndBankAccountId(@PathVariable Long bankAccountId) {
        Long count = transactionRepository.countTransactionsByMonthAndBankAccountId(bankAccountId);
        return ResponseEntity.ok().body(count);
    }
    @GetMapping("/{bankAccountId}/transaction-count-today")
    public ResponseEntity<Long> getTransactionCountByBankAccountIdToday(@PathVariable Long bankAccountId) {
        Long transactionCount = transactionRepository.getTransactionCountByBankAccountIdToday(bankAccountId);
        return ResponseEntity.ok().body(transactionCount);
    }
    @GetMapping("/{id}/highest-transaction")
    public ResponseEntity<?> getHighestTransaction(@PathVariable Long id) {
        Transactions highestTransaction = transactionRepository.findHighestTransactionByBankAccountId(id);
        if (highestTransaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(highestTransaction);
    }

    @GetMapping("/{id}/lowest-transaction")
    public ResponseEntity<?> getLowestTransaction(@PathVariable Long id) {
        Transactions lowestTransaction = transactionRepository.findLowestTransactionByBankAccountId(id);
        if (lowestTransaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lowestTransaction);
    }



}