package com.barclays.practice.springmvc.services;

import com.barclays.practice.springmvc.domain.BeerDTO;
import com.barclays.practice.springmvc.domain.BeerStyle;
import com.barclays.practice.springmvc.entities.Beer;
import com.barclays.practice.springmvc.mappers.BeerMapper;
import com.barclays.practice.springmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Page<BeerDTO> getBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
                                  Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        Page<Beer> beerPage;

        if (StringUtils.hasText(beerName) && beerStyle == null) {
            beerPage = getBeerByName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeerByStyle(beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeerByNameAndByStyle(beerName, beerStyle, pageRequest);
        }
        else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if (showInventory != null && !showInventory) {
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beerPage.map(beerMapper::beer);
    }

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    public Page<Beer> listBeerByNameAndByStyle(String beerName, BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageable);
    }

    public Page<Beer> listBeerByStyle(BeerStyle beerStyle, Pageable pageable) {
        System.out.println(pageable.getPageSize());
        return beerRepository.findAllByBeerStyle(beerStyle, pageable);
    }

    public Page<Beer> getBeerByName(String name, Pageable pageable) {
       // System.out.println(name);
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + name + "%", pageable);
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

        /*return Optional.of(beerMapper.beer(
                beerRepository.save(beerMapper.beerDTO(beer))
        ));*/

        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();
        beerRepository.findById(id).ifPresentOrElse(updateBeer -> {
            updateBeer.setBeerName(beer.getBeerName());
            updateBeer.setBeerStyle(beer.getBeerStyle());
            updateBeer.setUpc(beer.getUpc());
            updateBeer.setPrice(beer.getPrice());
            updateBeer.setVersion(beer.getVersion());
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
