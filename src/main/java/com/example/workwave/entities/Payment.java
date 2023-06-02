package com.example.workwave.entities;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Entity
@Table( name = "Payment")

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentNumber;

    private LocalDate paymentDate;

    private Double amountPaid;

    private String description;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String createdBy;

    private LocalDateTime createdAt;

    private Long senderBankAccountId;
    @ManyToOne
    private BankAccount bankAccount;

    public Payment() {

    }

    public Long getSenderBankAccountId() {
        return senderBankAccountId;
    }

    public void setSenderBankAccountId(Long senderBankAccountId) {
        this.senderBankAccountId = senderBankAccountId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", paymentNumber='" + paymentNumber + '\'' +
                ", paymentDate=" + paymentDate +
                ", amountPaid=" + amountPaid +
                ", description='" + description + '\'' +
                ", paymentStatus=" + paymentStatus +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt=" + createdAt +
                ", bankAccount=" + bankAccount +
                '}';
    }
}