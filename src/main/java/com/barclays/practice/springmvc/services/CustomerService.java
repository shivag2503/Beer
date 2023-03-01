package com.barclays.practice.springmvc.services;

import com.barclays.practice.springmvc.domain.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDTO> getCustomers();

    Optional<CustomerDTO> getCustomerById(UUID id);

    CustomerDTO createCustomer(CustomerDTO customer);

    void updateCustomer(UUID id, CustomerDTO customer);

    void deleteById(UUID id);

    void patchCustomerById(UUID id, CustomerDTO customer);
}
