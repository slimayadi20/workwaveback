package com.example.workwave.repositories;

import com.example.workwave.entities.BankAccount;
import com.example.workwave.entities.Role;
import com.example.workwave.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    public User findByBankAccount_Id(Long ID);

    public Optional<User> findByEmail(String email);

    Page<User> findByRole(Role role, Pageable pageable);
    List<User> findByRole(Role role);

    public List<User> findByEtat (String etat);

    public User findByToken(String token);

    public boolean existsByEmail(String email);
    public User findByBankAccount(BankAccount bankAccount);

    @Query("SELECT DISTINCT u " +
            "FROM User u " +
            "LEFT JOIN u.bankAccount b " +
            "LEFT JOIN b.payments p " +
            "WHERE b IS NOT NULL " +
            "AND (p.id IS NULL OR p.paymentDate < :date)")
    List<User> findUsersWithNoPaymentsInLast30DaysAndBankAccount(LocalDate date);

    @Query("SELECT DISTINCT u " +
            "FROM User u " +
            "LEFT JOIN u.bankAccount b " +
            "LEFT JOIN b.payments p " +
            "WHERE p.paymentDate >= :date " +
            "AND p.senderBankAccountId = :bankAccountId")
    List<User> findUsersWithPaymentsInLast29DaysForBankAccount(LocalDate date, Long bankAccountId);

}
