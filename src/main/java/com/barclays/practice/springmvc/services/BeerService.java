package com.barclays.practice.springmvc.services;

import com.barclays.practice.springmvc.domain.BeerDTO;
import com.barclays.practice.springmvc.domain.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    Page<BeerDTO> getBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
                           Integer pageNumber, Integer pageSize);

    Optional<BeerDTO> getBearById(UUID id);

    BeerDTO createBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer);

    boolean deleteById(UUID id);

    Optional<BeerDTO> patchBeerById(UUID id, BeerDTO beer);
}
