package com.barclays.practice.springmvc.services;

import com.barclays.practice.springmvc.domain.CustomerDTO;
import com.barclays.practice.springmvc.mappers.CustomerMapper;
import com.barclays.practice.springmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> getCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customer)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMapper
                .customer(customerRepository.findById(id).orElse(null)));
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customer) {
        return customerMapper.customer(customerRepository.save(customerMapper.customerDTO(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
        customerRepository.findById(id).ifPresentOrElse(updateCust -> {
            updateCust.setName(customer.getName());
            atomicReference.set(Optional.of(customerMapper
                    .customer(customerRepository.save(updateCust))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public boolean deleteById(UUID id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID id, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
        customerRepository.findById(id).ifPresentOrElse(updateCust -> {
            if (StringUtils.hasText(customer.getName())) {
                updateCust.setName(customer.getName());
            }
            atomicReference.set(Optional.of(customerMapper
                    .customer(customerRepository.save(updateCust))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }
}
