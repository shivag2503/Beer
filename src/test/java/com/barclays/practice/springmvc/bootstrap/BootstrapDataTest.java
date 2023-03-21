package com.barclays.practice.springmvc.bootstrap;

import com.barclays.practice.springmvc.repositories.BeerRepository;
import com.barclays.practice.springmvc.repositories.CustomerRepository;
import com.barclays.practice.springmvc.services.BeerCsvService;
import com.barclays.practice.springmvc.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)
class BootstrapDataTest {

   /* @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerCsvService beerCsvService;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepository, customerRepository, beerCsvService);
    }

    @Test
    void run() throws Exception {
        bootstrapData.run(null);

        assertThat(beerRepository.count()).isEqualTo(2411);
        assertThat(customerRepository.count()).isEqualTo(1);

    }
*/
}