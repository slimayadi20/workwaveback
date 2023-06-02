package com.example.workwave.services;

import com.example.workwave.entities.*;
import com.example.workwave.repositories.BankAccountRepository;
import com.example.workwave.repositories.PaymentRepository;
import com.example.workwave.repositories.TransactionRepository;
import com.example.workwave.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl {
    @Autowired
    ServletContext context;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private UserRepository userRepository;
    private String userName;
    private Long senderBankAccountId;
    private Long receiverBankAccountId;

    public List<Payment> GetAllPayments() {
        return paymentRepository.findAll();
    }

    public String addPayment(Payment P) {
        paymentRepository.save(P);
        return "ok";
    }

    public String deletePayment(Long idPay) {
        paymentRepository.deleteById(idPay);
        return "Budget removed !! " + idPay;
    }


    //the update method
    public Payment updatePayment(Payment payment) {
        Payment existingPayment = paymentRepository.findById(payment.getId()).orElse(null);
        existingPayment.setBankAccount(payment.getBankAccount());
        existingPayment.setPaymentDate(payment.getPaymentDate());
        existingPayment.setAmountPaid(payment.getAmountPaid());
        existingPayment.setPaymentNumber(payment.getPaymentNumber());
        existingPayment.setDescription(payment.getDescription());
        existingPayment.setPaymentStatus(payment.getPaymentStatus());
        existingPayment.setCreatedAt(payment.getCreatedAt());
        existingPayment.setCreatedBy(payment.getCreatedBy());
        existingPayment.setId(payment.getId());


        return paymentRepository.save(existingPayment);
    }

    public Payment getPaymentById(Long id) {
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if (optionalPayment.isPresent()) {
            return optionalPayment.get();
        } else {
            throw new EntityNotFoundException("Payment not found with id " + id);
        }
    }

    public void autoPay(String userName, Long senderBankAccountId, Long receiverBankAccountId) {
        this.userName = userName;
        this.senderBankAccountId = senderBankAccountId;
        this.receiverBankAccountId = receiverBankAccountId;
        scheduledAutoPay();
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void scheduledAutoPay() {
        // use the stored user and bank account IDs in the payment processing logic
        BankAccount senderBankAccount = bankAccountRepository.findById(senderBankAccountId).orElse(null);
        BankAccount receiverBankAccount = bankAccountRepository.findById(receiverBankAccountId).orElse(null);
        Payment payment = new Payment();
        payment.setPaymentNumber("AutoPay-" + LocalDate.now().toString());
        payment.setPaymentDate(LocalDate.now());
        User user = userRepository.findById(userName).get();
        payment.setAmountPaid((double) user.getSalary());
        payment.setDescription("AutoPay Salary");
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setCreatedBy("System");
        payment.setCreatedAt(LocalDateTime.now());
        payment.setBankAccount(receiverBankAccount);
        payment.setSenderBankAccountId(senderBankAccountId);
        paymentRepository.save(payment);
        Double salary = Double.valueOf(user.getSalary());
        if (senderBankAccount.getBalance() < salary) {
            throw new RuntimeException("Insufficient funds");
        }
        Double senderNewBalance = senderBankAccount.getBalance() - salary;
        Double receiverNewBalance = receiverBankAccount.getBalance() + salary;
        senderBankAccount.setBalance(senderNewBalance);
        receiverBankAccount.setBalance(receiverNewBalance);
        bankAccountRepository.save(senderBankAccount);
        bankAccountRepository.save(receiverBankAccount);
    }

    public String paySalary(User user, Long senderBankAccountId, Long receiverBankAccountId) {
        BankAccount senderBankAccount = bankAccountRepository.findById(senderBankAccountId).orElse(null);
        BankAccount receiverBankAccount = bankAccountRepository.findById(receiverBankAccountId).orElse(null);

        if (senderBankAccount == null || receiverBankAccount == null) {
            throw new RuntimeException("Sender or receiver bank account not found");
        }

        Double salary = Double.valueOf(user.getSalary());
        if (senderBankAccount.getBalance() < salary) {
            throw new RuntimeException("Insufficient funds");
        }

        Payment payment = new Payment();
        payment.setPaymentNumber("Salary-" + LocalDate.now().toString());
        payment.setPaymentDate(LocalDate.now());
        payment.setAmountPaid(salary);
        payment.setDescription("Salary Payment");
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setCreatedBy("System");
        payment.setCreatedAt(LocalDateTime.now());
        payment.setBankAccount(receiverBankAccount);
        payment.setSenderBankAccountId(senderBankAccountId);
        paymentRepository.save(payment);

        Transactions transaction = new Transactions();
        transaction.setAmount(-salary);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setDescription("Salary Payment");
        transaction.setBankAccount(senderBankAccount);
        transactionRepository.save(transaction);

        Transactions transactionR = new Transactions();
        transactionR.setAmount(salary);
        transactionR.setTransactionDate(LocalDate.now());
        transactionR.setDescription("Salary payment For You");
        transactionR.setBankAccount(receiverBankAccount);
        transactionRepository.save(transactionR);

        Double senderNewBalance = senderBankAccount.getBalance() - salary;
        Double receiverNewBalance = receiverBankAccount.getBalance() + salary;

        senderBankAccount.setBalance(senderNewBalance);
        receiverBankAccount.setBalance(receiverNewBalance);

        bankAccountRepository.save(senderBankAccount);
        bankAccountRepository.save(receiverBankAccount);
        return "Payment Successful";
    }

}
