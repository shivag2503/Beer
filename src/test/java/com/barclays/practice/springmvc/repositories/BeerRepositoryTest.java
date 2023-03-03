package com.barclays.practice.springmvc.repositories;

import com.barclays.practice.springmvc.bootstrap.BootstrapData;
import com.barclays.practice.springmvc.domain.BeerStyle;
import com.barclays.practice.springmvc.entities.Beer;
import com.barclays.practice.springmvc.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testBeerByName() {
        Page<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%", null);

        assertThat(list.getContent().size()).isEqualTo(336);
    }

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