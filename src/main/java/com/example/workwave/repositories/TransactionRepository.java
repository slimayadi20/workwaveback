package com.example.workwave.repositories;
import com.example.workwave.entities.BankAccount;
import com.example.workwave.entities.Budget;
import com.example.workwave.entities.Transactions;
import com.example.workwave.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
public interface TransactionRepository extends JpaRepository<Transactions,Long> {

    List<Transactions> findByBankAccount(BankAccount bankAccount);

    @Query("SELECT ABS(AVG(t.amount)) FROM Transactions t WHERE t.bankAccount.id = :bankAccountId")
    Double getAverageTransactionAmountByBankAccountId(@Param("bankAccountId") Long bankAccountId);
    @Query("SELECT COUNT(t) FROM Transactions t WHERE MONTH(t.transactionDate) = MONTH(CURRENT_DATE()) AND YEAR(t.transactionDate) = YEAR(CURRENT_DATE()) AND t.bankAccount.id = :bankAccountId")
    Long countTransactionsByMonthAndBankAccountId(@Param("bankAccountId") Long bankAccountId);
    @Query("SELECT COUNT(t) FROM Transactions t WHERE t.transactionDate = current_date() AND t.bankAccount.id = :bankAccountId")
    Long getTransactionCountByBankAccountIdToday(@Param("bankAccountId") Long bankAccountId);
    @Query("SELECT t.amount FROM Transactions t WHERE t.bankAccount = :bankAccount AND t.transactionDate >= :startDate AND t.transactionDate <= :endDate")
    List<Double> getBalanceChangesForPastMonth(@Param("bankAccount") BankAccount bankAccount, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query(value = "SELECT * FROM Transactions WHERE bank_account_id = :bankAccountId ORDER BY amount DESC LIMIT 1", nativeQuery = true)
    Transactions findHighestTransactionByBankAccountId(@Param("bankAccountId") Long bankAccountId);

    @Query(value = "SELECT * FROM Transactions WHERE bank_account_id = :bankAccountId ORDER BY amount ASC LIMIT 1", nativeQuery = true)
    Transactions findLowestTransactionByBankAccountId(@Param("bankAccountId") Long bankAccountId);

    List<Transactions> findByBankAccountAndTransactionDateBetween(BankAccount bankAccount, LocalDate startDate, LocalDate endDate);

    List<Transactions> findByBankAccountAndTransactionDateBetweenOrderByTransactionDateDesc(BankAccount bankAccount, LocalDate startDate, LocalDate currentDate);
}