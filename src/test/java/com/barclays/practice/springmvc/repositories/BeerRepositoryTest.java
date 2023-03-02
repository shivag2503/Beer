package com.barclays.practice.springmvc.repositories;

import com.barclays.practice.springmvc.domain.BeerStyle;
import com.barclays.practice.springmvc.entities.Beer;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeerNameTooLong() {

        assertThrows(ConstraintViolationException.class, () -> {
            Beer newBeer = beerRepository.save(Beer.builder()
                    .beerName("My Beer hjjshkjvnzkjlvhdjvxnckjvhxm njkvzcjk naskjbzvnsakjfhajkmasklfhsamvnajbjs")
                    .upc("4578554454")
                    .beerStyle(BeerStyle.LAGER)
                    .price(new BigDecimal("14.23"))
                    .build());
            beerRepository.flush();
        });
    }

    @Test
    void testSaveBeer() {
        Beer newBeer = beerRepository.save(Beer.builder()
                        .beerName("My Beer")
                        .upc("4578554454")
                        .beerStyle(BeerStyle.LAGER)
                        .price(new BigDecimal("14.23"))
                .build());

        beerRepository.flush();

        assertThat(newBeer).isNotNull();
        assertThat(newBeer.getId()).isNotNull();
    }
}