package com.barclays.practice.springmvc.controllers;

import com.barclays.practice.springmvc.domain.Customer;
import com.barclays.practice.springmvc.services.CustomerService;
import com.barclays.practice.springmvc.services.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> argumentCaptor;

    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;

    @MockBean
    CustomerService customerService;

    CustomerServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CustomerServiceImpl();
    }

    @Test
    void testPatchCustomer() throws Exception {
        Customer customer = service.getCustomers().get(0);

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "New Name");

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomerById(argumentCaptor.capture(),
                customerArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(argumentCaptor.getValue());
        assertThat(customerMap.get("beerName")).isEqualTo(customerArgumentCaptor.getValue().getName());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        Customer customer = service.getCustomers().get(0);

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService).deleteById(argumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(argumentCaptor.getValue());

    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = service.getCustomers().get(0);

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomer(any(UUID.class), any(Customer.class));
    }

    @Test
    void testCreateCustomer() throws Exception {
        Customer customer = service.getCustomers().get(0);
        customer.setVersion(null);
        customer.setId(null);

        given(customerService.createCustomer(any(Customer.class))).willReturn(service.
                getCustomers().get(1));

        mockMvc.perform(post(CustomerController.CUSTOMER_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void listCustomers() throws Exception {
        given(customerService.getCustomers()).willReturn(service.getCustomers());

        mockMvc.perform(get("/api/v1/customer")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void testCustomerById() throws Exception {
        Customer testCustomer = service.getCustomers().get(0);

        given(customerService.getCustomerById(testCustomer.getId())).willReturn(testCustomer);

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
                .andExpect(jsonPath("$.name", is(testCustomer.getName())));
    }

}