package com.barclays.practice.springmvc.controllers;

import com.barclays.practice.springmvc.congif.SpringSecurityConfig;
import com.barclays.practice.springmvc.domain.BeerDTO;
import com.barclays.practice.springmvc.services.BeerService;
import com.barclays.practice.springmvc.services.BeerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BeerController.class)
@Import(SpringSecurityConfig.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> argumentCaptor;

    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    @MockBean
    BeerService beerService;

    BeerServiceImpl beerImpl;

    public static final String USERNAME = "user1";
    public static  final  String PASSWORD = "password";

    @BeforeEach
    void setUp() {
        beerImpl = new BeerServiceImpl();
    }

    @Test
    void testCreateBeerNullBeerName() throws Exception {
        BeerDTO beerDTO = BeerDTO.builder().build();

        given(beerService.createBeer(any(BeerDTO.class)))
                .willReturn(beerImpl.getBeers(null, null, false, 1, 25)
                        .getContent().get(1));

        MvcResult mvcResult = mockMvc.perform(post(BeerController.BEER_URL)
                        .with(httpBasic(USERNAME, PASSWORD))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(6))).andReturn()
                ;

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testPatchBeer() throws Exception {
        BeerDTO beer = beerImpl.getBeers(null, null, false, 1, 25)
                .getContent().get(0);

        given(beerService.patchBeerById(any(), any())).willReturn(Optional.of(beer));

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchBeerById(argumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(argumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }

    @Test
    void testDeleteBeer() throws Exception {
        BeerDTO beer = beerImpl.getBeers(null, null, false, 1, 25)
                .getContent().get(0);

        given(beerService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete(BeerController.BEER_PATH_ID, beer.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteById(argumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(argumentCaptor.getValue());
    }

    @Test
    void testUpdateBeer() throws Exception {
        BeerDTO beer = beerImpl.getBeers(null, null, false, 1, 25)
                .getContent().get(0);

        given(beerService.updateBeer(any(), any())).willReturn(Optional.of(beer));

        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());

        verify(beerService).updateBeer(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void testUpdateBeerBlankName() throws Exception {
        BeerDTO beer = beerImpl.getBeers(null, null, false, 1, 25)
                .getContent().get(0);
        beer.setBeerName("");

        given(beerService.updateBeer(any(), any())).willReturn(Optional.of(beer));

        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void createNewBeerTest() throws Exception {
        BeerDTO beer = beerImpl.getBeers(null, null, false, 1, 25)
                .getContent().get(0);
        beer.setVersion(null);
        beer.setId(null);

        given(beerService.createBeer(any(BeerDTO.class))).willReturn(beerImpl
                .getBeers(null, null, false, 1, 25).getContent().get(1));

        mockMvc.perform(post("/api/v1/beer")
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void getBeerByIdNotFound() throws Exception {

        given(beerService.getBearById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID())
                        .with(httpBasic(USERNAME, PASSWORD)))

                .andExpect(status().isNotFound());
    }

    @Test
    void testListBeers() throws Exception {
        given(beerService.getBeers(null, null, false, null, null))
                .willReturn(beerImpl.getBeers(null, null, false, null, null));

        mockMvc.perform(get(BeerController.BEER_URL)
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(3)));
    }

    @Test
    void getBeerById() throws Exception {

        BeerDTO testBeer = beerImpl.getBeers(null, null, false, 1, 25)
                .getContent().get(0);

        given(beerService.getBearById(testBeer.getId())).willReturn(Optional.of(testBeer));

        mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }
}