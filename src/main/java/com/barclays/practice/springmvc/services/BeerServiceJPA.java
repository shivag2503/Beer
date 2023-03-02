package com.barclays.practice.springmvc.services;

import com.barclays.practice.springmvc.domain.BeerDTO;
import com.barclays.practice.springmvc.mappers.BeerMapper;
import com.barclays.practice.springmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> getBeers() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::beer)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BeerDTO> getBearById(UUID id) {
        return Optional.ofNullable(beerMapper.beer
                (beerRepository.findById(id)
                        .orElse(null)));
    }

    @Override
    public BeerDTO createBeer(BeerDTO beer) {
        return beerMapper.beer(beerRepository.save(beerMapper.beerDTO(beer)));
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer) {

        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();
        beerRepository.findById(id).ifPresentOrElse(updateBeer -> {
            updateBeer.setBeerName(beer.getBeerName());
            updateBeer.setBeerStyle(beer.getBeerStyle());
            updateBeer.setUpc(beer.getUpc());
            updateBeer.setPrice(beer.getPrice());
            atomicReference.set(Optional.of(beerMapper
                    .beer(beerRepository.save(updateBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public boolean deleteById(UUID id) {
        if (beerRepository.existsById(id)) {
            beerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID id, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();
        beerRepository.findById(id).ifPresentOrElse(updateBeer -> {
            if (StringUtils.hasText(beer.getBeerName())) {
                updateBeer.setBeerName(beer.getBeerName());
            }
            if (StringUtils.hasText(beer.getUpc())) {
                updateBeer.setUpc(beer.getUpc());
            }
            if (beer.getBeerStyle() != null) {
                updateBeer.setBeerStyle(beer.getBeerStyle());
            }
            if (beer.getQuantityOnHand() != null) {
                updateBeer.setQuantityOnHand(beer.getQuantityOnHand());
            }
            if (beer.getPrice() != null) {
                updateBeer.setPrice(beer.getPrice());
            }
            atomicReference.set(Optional.of(beerMapper.beer(beerRepository.save(updateBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
