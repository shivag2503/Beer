package com.barclays.practice.springmvc.services;

import com.barclays.practice.springmvc.domain.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<Beer> getBeers();

    Optional<Beer> getBearById(UUID id);

    Beer createBeer(Beer beer);

    void updateBeer(UUID id, Beer beer);

    void deleteById(UUID id);

    void patchBeerById(UUID id, Beer beer);
}
