package com.example.workwave.services;

import com.example.workwave.entities.BankAccount;
import com.example.workwave.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletContext;
import java.time.LocalDate;
import java.util.*;

@Service

public class BankAccountServiceImpl {
    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    ServletContext context;

    public List<BankAccount> GetAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    public String addBankAccount(BankAccount b) {
        bankAccountRepository.save(b);
        return "ok";
    }


    //the update method
    public BankAccount updateBankAccount(BankAccount bankAccount) {
        BankAccount existingBankAccount = bankAccountRepository.findById(bankAccount.getId()).orElse(null);
        existingBankAccount.setAccountName(bankAccount.getAccountName());
        existingBankAccount.setAccountNumber(bankAccount.getAccountNumber());
        existingBankAccount.setBankName(bankAccount.getBankName());
        existingBankAccount.setBalance(bankAccount.getBalance());
        existingBankAccount.setUser(bankAccount.getUser());
        existingBankAccount.setPayments(bankAccount.getPayments());
        existingBankAccount.setInvoices(bankAccount.getInvoices());
        existingBankAccount.setStatus(bankAccount.getStatus());
        existingBankAccount.setLimitAmount(bankAccount.getLimitAmount());
        existingBankAccount.setTransactions(bankAccount.getTransactions());
        existingBankAccount.setId(existingBankAccount.getId());


        return bankAccountRepository.save(existingBankAccount);
    }

    public BankAccount getBankAccountById(Long id) {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(id);
        if (optionalBankAccount.isPresent()) {
            return optionalBankAccount.get();
        } else {
            throw new EntityNotFoundException("Bank Account not found with id " + id);
        }
    }

    //**************************************************************************************
    public List<Map<String, Object>> getBalanceHistoryWithPercentageChange(Long accountId, Double currentBalance) {
        List<Object[]> balanceHistory = bankAccountRepository.getBalanceHistory(accountId);
        List<Map<String, Object>> result = new ArrayList<>();
        Double previousBalance = currentBalance;
        for (Object[] row : balanceHistory) {
            Map<String, Object> data = new HashMap<>();
            LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
            Double newbalance = (Double) row[1];
            if (newbalance == null || newbalance == 0.0) {
                newbalance = previousBalance;
            } else {
                previousBalance += newbalance;
            }

            Double percentageChange = previousBalance != null && previousBalance != 0.0 ? ((previousBalance - newbalance) / newbalance) / 100 : null;

            data.put("date", date);
            data.put("balance", previousBalance);
            data.put("percentageChange", percentageChange);
            result.add(data);
        }
        return result;
    }


}