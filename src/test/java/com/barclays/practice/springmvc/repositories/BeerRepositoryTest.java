package com.barclays.practice.springmvc.repositories;

import com.barclays.practice.springmvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer newBeer = beerRepository.save(Beer.builder()
                        .beerName("My Beer")
                .build());

        assertThat(newBeer).isNotNull();
        assertThat(newBeer.getId()).isNotNull();
    }
}