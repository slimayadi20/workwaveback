package com.example.workwave.services;

import com.example.workwave.entities.Invoices;
import com.example.workwave.entities.Order;
import com.example.workwave.entities.products;
import com.example.workwave.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl {

    @Autowired
    OrderRepository OrderRepository;

    @Autowired
    ServletContext context;
    @Autowired
    productsRepository productsRepository;
    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    EntityManager entityManager;

    @Autowired
    InvoicesRepository invoicesRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    RoleRepository roleRepository;

    public List<Order> GetAllOrder() {
        return OrderRepository.findAll();
    }

    public String addOrder(Order o) {

        OrderRepository.save(o);
        return "ok";
    }


    public String deleteOrder(long o_id) {
        OrderRepository.deleteById(o_id);
        return "Order removed !!" + o_id;
    }

    public Order updateOrder(Order order) {


        return OrderRepository.save(order);
    }

    public void addOrderToProduct(long o_id, long p_id) {
        Order order = OrderRepository.findById(o_id).orElse(null);
        products product = productsRepository.findById(p_id).orElse(null);
        if (order != null && product != null) {
            product.setOrder(order);
            productsRepository.save(product);
        }
    }

    public Order getorder(long o_id) {
        return entityManager.find(Order.class, o_id);

    }

    public void addInvoice(Long o_id, String userName) {
        Invoices invoices = new Invoices();
        invoices.setIssueDate(LocalDate.now());
        invoices.setInvoiceNumber("ORDER-" + o_id);
        invoices.setStatus("To Pay");
        invoices.setOrder(OrderRepository.findById(o_id).get());
        invoices.setDescription("Invoice for Order Number " + o_id);
        BigDecimal amount = BigDecimal.valueOf(Integer.valueOf(OrderRepository.findById(o_id).get().getQuantity()) * productsRepository.getProductPricesByOrderId(o_id));
        invoices.setAmountDue(amount);
        invoices.setCreatedBy(userName);
        invoices.setBankAccount(bankAccountRepository.findByUserUserName(String.valueOf(userRepository.findById(userRepository.findByRole(roleRepository.findRoleByRoleName("Financial Manager"), (Pageable) userRepository.findAllById(Collections.singleton(userName))).toString()))));
        invoicesRepository.save(invoices);
    }


}