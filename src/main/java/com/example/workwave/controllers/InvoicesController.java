package com.example.workwave.controllers;

import com.example.workwave.entities.*;

import com.example.workwave.exception.ResourceNotFoundException;
import com.example.workwave.repositories.*;
import com.example.workwave.services.BankAccountServiceImpl;
import com.example.workwave.services.BudgetServiceImpl;
import com.example.workwave.services.InvoicesServiceImpl;
import com.example.workwave.services.ProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
@RestController
public class InvoicesController {
    @Autowired
    InvoicesRepository invoicesRepository;
    @Autowired
    InvoicesServiceImpl invoicesService;
    @Autowired
    BankAccountRepository bankAccountRepository;

    @PostMapping("/addInvoice")
    public String addInvoice(@RequestBody Invoices invoices) {
        return invoicesService.addInvoice(invoices);
    }

    @DeleteMapping("/deleteInvoice/{id}")
    public String deleteInvoice(@PathVariable long id) {
        return invoicesService.deleteInvoice(id);
    }

    @PutMapping("/updateInvoice")
    public Invoices updateInvoice(@RequestBody Invoices invoices) {
        return invoicesService.updateInvoice(invoices);
    }

    @GetMapping("/Invoices")//affichage+pagination
    public List<Invoices> show() {
        return invoicesService.GetAllInvoices();
    }

    @GetMapping("/Invoice/{id}")
    public Invoices getInvoiceById(@PathVariable Long id) {
        return invoicesService.getInvoiceById(id);
    }
    @GetMapping("/InvoicesbyBankAccount/{id}")
    public List<Invoices> getInvoiceByBank(@PathVariable Long id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank Account not found"));

        List<Invoices> invoices = invoicesRepository.getInvoicesByBankAccount(bankAccount);
        if (invoices == null) {
            throw new RuntimeException("Transaction not found for Bank Account");
        }
        return invoices;
    }
    @PutMapping("/{id}/pay")
    public ResponseEntity<Invoices> payInvoice(@PathVariable(value = "id") Long invoiceId,
                                               @RequestParam(value = "bankAccountId") Long bankAccountId) {
        Invoices invoice = invoicesRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + invoiceId));

        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found with id: " + bankAccountId));

        BigDecimal amountDue = invoice.getAmountDue();
        BigDecimal balance = BigDecimal.valueOf(bankAccount.getBalance());

        if (balance.compareTo(amountDue) >= 0) { // check if balance is sufficient to pay the invoice
            invoice.setStatus("Paid");
            invoice.setUpdatedAt(LocalDateTime.now());

            bankAccount.setBalance(Double.valueOf(String.valueOf(balance.subtract(amountDue)))); // reduce the bank account balance

            invoicesRepository.save(invoice);
            bankAccountRepository.save(bankAccount);

            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}