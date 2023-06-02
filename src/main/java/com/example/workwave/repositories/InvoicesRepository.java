package com.example.workwave.repositories;
import com.example.workwave.entities.BankAccount;
import com.example.workwave.entities.Invoices;
import com.example.workwave.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface InvoicesRepository extends JpaRepository<Invoices,Long> {
    public List<Invoices> getInvoicesByBankAccount(BankAccount bankAccount);
}