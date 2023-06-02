package com.example.workwave.services;

import com.example.workwave.entities.*;
import com.example.workwave.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletContext;
import java.time.LocalDate;
import java.util.*;

@Service
public class BudgetServiceImpl {
    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    ServletContext context;

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    public Budget requestBudget(Long pID, Double Amount, Long bID) {
        Project project = projectRepository.findById(pID)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        BankAccount bankAccount = bankAccountRepository.findById(bID)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        Budget budget = new Budget();
        budget.setName(project.getProjectname() + " Budget");
        budget.setAmount(Amount);
        budget.setStartDate(new Date());
        budget.setEndDate(project.getDateExpiration());
        budget.setNotes(project.getDescription());
        budget.setStatusBudget(StatusBudget.InProgress);
        budget.setProject(project);
        budget.setBankAccount(bankAccount);
        project.setBudget(budget);
        budgetRepository.save(budget);
        projectRepository.save(project);




        return budget;
    }

    public Budget approveBudget(Long budgetId) {


        Budget budget = budgetRepository.findById(budgetId).orElse(null);
        if (budget == null) {
            throw new RuntimeException("Budget not found");
        }

        BankAccount bankAccount = budget.getBankAccount();
        bankAccount.setBalance(bankAccount.getBalance() - budget.getAmount());
        Transactions transaction = new Transactions();
        transaction.setAmount(-budget.getAmount());
        transaction.setTransactionDate(LocalDate.now());
        transaction.setDescription("Budget Payment");
        transaction.setBankAccount(bankAccount);
        transactionRepository.save(transaction);


        budget.setStatusBudget(StatusBudget.Approved);
        budgetRepository.save(budget);

        return budget;
    }

    public Budget declineBudget(Long budgetId) {


        Budget budget = budgetRepository.findById(budgetId).orElse(null);
        if (budget == null) {
            throw new RuntimeException("Budget not found");
        }

        budget.setStatusBudget(StatusBudget.Declined);
        budgetRepository.save(budget);

        return budget;
    }

    public List<Budget> GetAllBudgets() {
        return budgetRepository.findAll();
    }

    public String addBudget(Budget b) {
        budgetRepository.save(b);
        return "ok";
    }

    public String deleteBudget(Long idBudget) {
        budgetRepository.deleteById(idBudget);
        return "Budget removed !! " + idBudget;
    }


    //the update method
    public Budget updateBudget(Budget budget) {
        Budget existingBudget = budgetRepository.findById(budget.getId()).orElse(null);
        existingBudget.setBankAccount(budget.getBankAccount());
        existingBudget.setAmount(budget.getAmount());
        existingBudget.setName(budget.getName());
        existingBudget.setStartDate(budget.getStartDate());
        existingBudget.setEndDate(budget.getEndDate());
        existingBudget.setNotes(budget.getNotes());
        existingBudget.setProject(budget.getProject());
        existingBudget.setId(budget.getId());


        return budgetRepository.save(existingBudget);
    }

    public Budget getBudgetById(Long id) {
        Optional<Budget> optionalBudget = budgetRepository.findById(id);
        if (optionalBudget.isPresent()) {
            return optionalBudget.get();
        } else {
            throw new EntityNotFoundException("Bank Account not found with id " + id);
        }
    }// Method to get the project name and budget amount of the highest budget for a specific bankAccount

    // Method to get the project name and budget amount of the highest budget for a specific bankAccount
    public Map<String, Object> getHighestBudgetForBankAccount(Long bankAccountId) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElse(null);
        if (bankAccount == null) {
            // BankAccount not found
            return null;
        }

        List<Budget> budgets = bankAccount.getBudget();
        if (budgets == null || budgets.isEmpty()) {
            // No budget found for this bankAccount
            return null;
        }

        // Sort the budgets in descending order based on the amount
        Collections.sort(budgets, Comparator.comparing(Budget::getAmount).reversed());

        // Get the project name and budget amount of the highest budget
        Budget highestBudget = budgets.get(0);
        String projectName = highestBudget.getProject().getProjectname();
        Double budgetAmount = highestBudget.getAmount();

        Map<String, Object> result = new HashMap<>();
        result.put("projectName", projectName);
        result.put("budgetAmount", budgetAmount);
        return result;
    }


    // Method to get the project name and budget amount of the lowest budget for a specific bankAccount
    public Map<String, Object> getLowestBudgetForBankAccount(Long bankAccountId) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElse(null);
        if (bankAccount == null) {
            // BankAccount not found
            return null;
        }

        List<Budget> budgets = bankAccount.getBudget();
        if (budgets == null || budgets.isEmpty()) {
            // No budget found for this bankAccount
            return null;
        }

        // Sort the budgets in ascending order based on the amount
        Collections.sort(budgets, Comparator.comparing(Budget::getAmount));

        // Get the project name and budget amount of the lowest budget
        Budget lowestBudget = budgets.get(0);
        String projectName = lowestBudget.getProject().getProjectname();
        Double budgetAmount = lowestBudget.getAmount();

        Map<String, Object> result = new HashMap<>();
        result.put("projectName", projectName);
        result.put("budgetAmount", budgetAmount);
        return result;
    }


}