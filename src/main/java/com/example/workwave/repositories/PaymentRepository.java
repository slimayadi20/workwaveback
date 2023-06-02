package com.example.workwave.repositories;

import com.example.workwave.entities.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    List<Payment> findByBankAccount_Id(long id);

    @Query("SELECT SUM(p.amountPaid) FROM Payment p WHERE p.senderBankAccountId = :senderBankAccountId")
    Double getTotalAmountPaidBySenderBankAccountId(@Param("senderBankAccountId") Long senderBankAccountId);

    @Query("SELECT p.amountPaid FROM Payment p WHERE p.senderBankAccountId = :senderBankAccountId AND YEAR(p.paymentDate) = YEAR(CURRENT_DATE()) AND MONTH(p.paymentDate) = MONTH(CURRENT_DATE())")
    List<Double> getPaymentsBySenderBankAccountIdThisMonth(@Param("senderBankAccountId") Long senderBankAccountId);

    @Query("SELECT p.amountPaid FROM Payment p WHERE p.senderBankAccountId = :senderBankAccountId AND p.paymentDate = CURRENT_DATE()")
    List<Double> getPaymentsBySenderBankAccountIdToday(@Param("senderBankAccountId") Long senderBankAccountId);

    @Query("SELECT SUM(p.amountPaid) FROM Payment p WHERE p.senderBankAccountId = :senderBankAccountId AND DATE(p.paymentDate) = CURRENT_DATE()")
    Double getTotalAmountPaidToday(@Param("senderBankAccountId") Long senderBankAccountId);

    @Query("SELECT SUM(p.amountPaid) FROM Payment p WHERE p.senderBankAccountId = :senderBankAccountId AND DATE(p.paymentDate) =:yesterdayDate")
    Double getTotalAmountPaidYesterday(@Param("senderBankAccountId") Long senderBankAccountId, @Param("yesterdayDate") Date yesterdayDate);

    @Query("SELECT p FROM Payment p WHERE p.bankAccount.id = :bankAccountId AND p.amountPaid = (SELECT MAX(p2.amountPaid) FROM Payment p2 WHERE p2.bankAccount.id = :bankAccountId)")
    Payment findHighestPaymentByBankAccount(@Param("bankAccountId") Long bankAccountId);

    @Query("SELECT p FROM Payment p WHERE p.bankAccount.id = :bankAccountId AND p.amountPaid = (SELECT MIN(p2.amountPaid) FROM Payment p2 WHERE p2.bankAccount.id = :bankAccountId)")
    Payment findLowestPaymentByBankAccount(@Param("bankAccountId") Long bankAccountId);

}