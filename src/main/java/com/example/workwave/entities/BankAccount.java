package com.example.workwave.entities;

import com.fasterxml.jackson.annotation.*;
import com.example.workwave.entities.Budget;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Entity
@Table( name = "BankAccount")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler","Invoices","Transactions"})
public class BankAccount  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String accountName;

    private String accountNumber;

    private Double balance;

    private Double limitAmount;

    private String bankName;

    private Boolean status;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL)
    private List<Budget> budget;
    @JsonManagedReference
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL)
    private List<Invoices> invoices;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @OneToOne(mappedBy = "bankAccount")
    private User user;

    @OneToMany(mappedBy = "bankAccount",cascade = CascadeType.ALL)
    private List<Transactions> transactions;
    public BankAccount() {
    }

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Budget> getBudget() {
        return budget;
    }

    public void setBudget(List<Budget> budget) {
        this.budget = budget;
    }

    public List<Invoices> getInvoices() {
        return invoices;
    }



    public void setInvoices(List<Invoices> invoices) {
        this.invoices = invoices;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(Double limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }



    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", accountName='" + accountName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", limitAmount=" + limitAmount +
                ", bankName='" + bankName + '\'' +
                ", status=" + status +
                ", budget=" + budget +
                ", invoices=" + invoices +
                ", payments=" + payments +
                ", user=" + user +
                ", transactions=" + transactions +
                '}';
    }
}