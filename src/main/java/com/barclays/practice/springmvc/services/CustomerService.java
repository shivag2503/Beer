package com.barclays.practice.springmvc.services;

import com.barclays.practice.springmvc.domain.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<Customer> getCustomers();

    Optional<Customer> getCustomerById(UUID id);

    Customer createCustomer(Customer customer);

    void updateCustomer(UUID id, Customer customer);

    void deleteById(UUID id);

    void patchCustomerById(UUID id, Customer customer);
}
