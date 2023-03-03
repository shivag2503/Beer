package com.barclays.practice.springmvc.controllers;

import com.barclays.practice.springmvc.domain.BeerDTO;
import com.barclays.practice.springmvc.domain.BeerStyle;
import com.barclays.practice.springmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {

    public static final String BEER_URL = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_URL + "/{beerId}";
    private final BeerService beerService;

    @PostMapping(BEER_URL)
    public ResponseEntity handlePost(@Validated @RequestBody BeerDTO beer) {
        BeerDTO newBeer = beerService.createBeer(beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_URL + "/" + newBeer.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID id, @Validated @RequestBody BeerDTO beer) {
        if (beerService.updateBeer(id, beer).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity patchBeerById(@PathVariable("beerId") UUID id, @RequestBody BeerDTO beer) {
        if (beerService.patchBeerById(id, beer).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteBeer(@PathVariable("beerId") UUID id) {
        if (!beerService.deleteById(id)) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(BEER_URL)
    public Page<BeerDTO> listBeers(@RequestParam(required = false) String beerName,
                                   @RequestParam(required = false) BeerStyle beerStyle,
                                   @RequestParam(required = false) boolean showInventory,
                                   @RequestParam(required = false) Integer pageNumber,
                                   @RequestParam(required = false) Integer pageSize) {
        return beerService.getBeers(beerName, beerStyle, showInventory, pageNumber,
                pageSize);
    }

    @GetMapping(BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID id) {

        log.debug("Get beer id - in controller class -111111111");
        return beerService.getBearById(id).orElseThrow(NotFoundException::new);
    }
}
