package com.example.LibManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.LibManager.models.Customer;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByCustomerID(String customerID);
    Optional<Customer> findByCustomerName(String customerName);
}
