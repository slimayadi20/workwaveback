package com.example.workwave.services;

import com.example.workwave.entities.Invoices;
import com.example.workwave.repositories.InvoicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletContext;
import java.util.List;
import java.util.Optional;

@Service
public class InvoicesServiceImpl {
    @Autowired
    InvoicesRepository invoicesRepository;

    @Autowired
    ServletContext context;

    public List<Invoices> GetAllInvoices() {
        return invoicesRepository.findAll();
    }

    public String addInvoice(Invoices b) {
        invoicesRepository.save(b);
        return "ok";
    }

    public String deleteInvoice(Long idInvoice) {
        invoicesRepository.deleteById(idInvoice);
        return "Invoice removed !! " + idInvoice;
    }


    //the update method
    public Invoices updateInvoice(Invoices invoices) {
        Invoices existingInvoice = invoicesRepository.findById(invoices.getId()).orElse(null);
        existingInvoice.setBankAccount(invoices.getBankAccount());
        existingInvoice.setAmountDue(invoices.getAmountDue());
        existingInvoice.setInvoiceNumber(invoices.getInvoiceNumber());
        existingInvoice.setUpdatedAt(invoices.getUpdatedAt());
        existingInvoice.setUpdatedBy(invoices.getUpdatedBy());
        existingInvoice.setCreatedAt(invoices.getCreatedAt());
        existingInvoice.setDescription(invoices.getDescription());
        existingInvoice.setStatus(invoices.getStatus());
        existingInvoice.setDueDate(invoices.getDueDate());
        existingInvoice.setIssueDate(invoices.getIssueDate());
        existingInvoice.setId(invoices.getId());
        existingInvoice.setCreatedBy(invoices.getCreatedBy());


        return invoicesRepository.save(existingInvoice);
    }

    public Invoices getInvoiceById(Long id) {
        Optional<Invoices> optionalInvoices = invoicesRepository.findById(id);
        if (optionalInvoices.isPresent()) {
            return optionalInvoices.get();
        } else {
            throw new EntityNotFoundException("Invoices not found with id " + id);
        }
    }

}