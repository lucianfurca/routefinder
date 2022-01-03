package com.tripplanner.routefinder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripplanner.routefinder.services.RouteFinderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
class RouteFinderControllerTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private MockMvc mockMvc;

    @Autowired
    private RouteFinderService routeFinderService;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void givenTwoCountriesWithNeighbours_WhenProcessingRoute_ThenTheCorrectRouteShouldBeRetrieved()
            throws Exception {

        String routeJson = "{\"route\":[\"CZE\",\"AUT\",\"ITA\"]}";

        mockMvc.perform(get("/routing/" + "CZE" +"/" + "ITA"))
                .andExpect(status().isOk())
                .andExpect(content().string(routeJson));
    }

    @Test
    void givenTwoCountriesWithoutNeighbours_WhenProcessingRoute_ThenHTTP400ShouldBeReturned()
            throws Exception {

        mockMvc.perform(get("/routing/" + "ATF" +"/" + "ATA"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenTwoNonExistingCountries_WhenProcessingRoute_ThenHTTP400ShouldBeReturned()
            throws Exception {

        mockMvc.perform(get("/routing/" + "AGWEGWTF" +"/" + "GEWGWE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenOneCountriesWithNoPossibleRouteBetweenThem_WhenProcessingRoute_ThenHTTP400ShouldBeReturned()
            throws Exception {

        mockMvc.perform(get("/routing/" + "USA" +"/" + "ITA"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenOneCountryWithNeighboursAndOneWithout_WhenProcessingRoute_ThenHTTP400ShouldBeReturned()
            throws Exception {

        mockMvc.perform(get("/routing/" + "ITA" +"/" + "ATA"))
                .andExpect(status().isBadRequest());
    }

}
