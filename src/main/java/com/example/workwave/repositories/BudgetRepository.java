package com.example.workwave.repositories;
import com.example.workwave.entities.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface BudgetRepository extends JpaRepository<Budget,Long> {

Budget getBudgetByProject(Project project);
    List <Budget> getBudgetByBankAccountAndStatusBudget(BankAccount bankAccount,StatusBudget statusBudget);
    List<Budget> findByStatusBudget(StatusBudget statusBudget);
    @Query("SELECT COUNT(b) FROM Budget b")
    Long countBudgets();

}