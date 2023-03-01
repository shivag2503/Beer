package com.barclays.practice.springmvc.services;

import com.barclays.practice.springmvc.domain.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        CustomerDTO customer1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Shane Watson")
                .version(1)
                .createdTime(LocalDateTime.now())
                .lastModifiedTime(LocalDateTime.now())
                .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Ratan Tata")
                .version(1)
                .createdTime(LocalDateTime.now())
                .lastModifiedTime(LocalDateTime.now())
                .build();

        CustomerDTO customer3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Mukesh Ambani")
                .version(1)
                .createdTime(LocalDateTime.now())
                .lastModifiedTime(LocalDateTime.now())
                .build();

        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
    }

    @Override
    public List<CustomerDTO> getCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        log.debug("In customer service");
        return Optional.of(customerMap.get(id));
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customer) {
        CustomerDTO newCustomer = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name(customer.getName())
                .version(customer.getVersion())
                .createdTime(LocalDateTime.now())
                .lastModifiedTime(LocalDateTime.now())
                .build();

        customerMap.put(newCustomer.getId(), newCustomer);
        return newCustomer;
    }

    @Override
    public void updateCustomer(UUID id, CustomerDTO customer) {
        CustomerDTO existingCustomer = customerMap.get(id);
        existingCustomer.setName(customer.getName());
        existingCustomer.setVersion(customer.getVersion());
        existingCustomer.setLastModifiedTime(LocalDateTime.now());
        customerMap.put(id, existingCustomer);
    }

    @Override
    public void deleteById(UUID id) {
        customerMap.remove(id);
    }

    @Override
    public void patchCustomerById(UUID id, CustomerDTO customer) {
        CustomerDTO existingCustomer = customerMap.get(id);

        if (customer.getVersion() != null) {
            existingCustomer.setVersion(customer.getVersion());
        }
    }
}
