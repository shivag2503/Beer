package com.barclays.practice.springmvc.services;

import com.barclays.practice.springmvc.domain.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<BeerDTO> getBeers();

    Optional<BeerDTO> getBearById(UUID id);

    BeerDTO createBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer);

    boolean deleteById(UUID id);

    Optional<BeerDTO> patchBeerById(UUID id, BeerDTO beer);
}
