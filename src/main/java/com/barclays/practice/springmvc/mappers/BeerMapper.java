package com.barclays.practice.springmvc.mappers;

import com.barclays.practice.springmvc.domain.BeerDTO;
import com.barclays.practice.springmvc.entities.Beer;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDTO(BeerDTO dto);
    BeerDTO beer(Beer beer);
}
