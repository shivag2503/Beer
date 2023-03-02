package com.barclays.practice.springmvc.bootstrap;

import com.barclays.practice.springmvc.domain.BeerStyle;
import com.barclays.practice.springmvc.entities.Beer;
import com.barclays.practice.springmvc.entities.Customer;
import com.barclays.practice.springmvc.repositories.BeerRepository;
import com.barclays.practice.springmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
    }

    private void loadCustomerData() {
        if (customerRepository.count() == 0) {
            Customer customer1 = Customer.builder()
                    .name("Shankar Yadav")
                    .createdTime(LocalDateTime.now())
                    .lastModifiedTime(LocalDateTime.now())
                    .build();

            customerRepository.save(customer1);
        }
    }

    private void loadBeerData() {
        if (beerRepository.count() == 0) {
            Beer beer1 = Beer.builder()
                    .beerName("Galaxy cat")
                    .beerStyle(BeerStyle.LAGER)
                    .price(new BigDecimal(24.20))
                    .upc("7458950")
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            beerRepository.save(beer1);
        }
    }
}
