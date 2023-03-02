package com.barclays.practice.springmvc.mappers;

import com.barclays.practice.springmvc.domain.CustomerDTO;
import com.barclays.practice.springmvc.entities.Customer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDTO(CustomerDTO dto);
    CustomerDTO customer(Customer customer);
}
