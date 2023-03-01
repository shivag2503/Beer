package com.barclays.practice.springmvc.services;

import com.barclays.practice.springmvc.domain.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<BeerDTO> getBeers();

    Optional<BeerDTO> getBearById(UUID id);

    BeerDTO createBeer(BeerDTO beer);

    void updateBeer(UUID id, BeerDTO beer);

    void deleteById(UUID id);

    void patchBeerById(UUID id, BeerDTO beer);
}
