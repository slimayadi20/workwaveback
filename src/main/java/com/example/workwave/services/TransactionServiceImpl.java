package com.example.workwave.services;

import com.example.workwave.entities.BankAccount;
import com.example.workwave.entities.Transactions;
import com.example.workwave.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletContext;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ServletContext context;

    public List<Transactions> GetAllTransactions() {
        return transactionRepository.findAll();
    }

    public String addTransaction(Transactions T) {
        transactionRepository.save(T);
        return "ok";
    }

    public String deleteTransaction(Long idPay) {
        transactionRepository.deleteById(idPay);
        return "Budget removed !! " + idPay;
    }


    //the update method
    public Transactions updateTransaction(Transactions transactions) {
        Transactions existingTransaction = transactionRepository.findById(transactions.getId()).orElse(null);
        existingTransaction.setBankAccount(transactions.getBankAccount());
        existingTransaction.setTransactionDate(transactions.getTransactionDate());
        existingTransaction.setDescription(transactions.getDescription());
        existingTransaction.setAmount(transactions.getAmount());
        existingTransaction.setId(transactions.getId());


        return transactionRepository.save(existingTransaction);
    }

    public Transactions getTransactionById(Long id) {
        Optional<Transactions> optionalTransactions = transactionRepository.findById(id);
        if (optionalTransactions.isPresent()) {
            return optionalTransactions.get();
        } else {
            throw new EntityNotFoundException("Transaction not found with id " + id);
        }
    }

    public List<Transactions> GetTransactionsByBankAccount(BankAccount bankAccount) {
        return transactionRepository.findByBankAccount(bankAccount);
    }

}