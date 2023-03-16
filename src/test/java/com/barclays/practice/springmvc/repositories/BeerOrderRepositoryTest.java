package com.barclays.practice.springmvc.repositories;

import com.barclays.practice.springmvc.entities.Beer;
import com.barclays.practice.springmvc.entities.BeerOrder;
import com.barclays.practice.springmvc.entities.BeerOrderShipment;
import com.barclays.practice.springmvc.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    Customer testCustomer;
    Beer testBeer;

    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.findAll().get(0);
        testBeer = beerRepository.findAll().get(0);
    }

    @Transactional
    @Test
    void testBeerOrders() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("Test order")
                .customer(testCustomer)
                .beerOrderShipment(BeerOrderShipment.builder()
                        .trackingNumber("12456n")
                        .build())
                .build();

        BeerOrder saveBeerOrder = beerOrderRepository.save(beerOrder);

        System.out.println(saveBeerOrder.getCustomerRef());
    }

}