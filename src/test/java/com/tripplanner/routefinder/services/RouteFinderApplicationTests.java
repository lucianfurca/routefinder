package com.tripplanner.routefinder.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RouteFinderApplicationTests {

    @Autowired
    private RouteFinderService routeFinderService;


    @Test
    void givenTwoValidCountriesWithNeighbours_WhenProcessingRoute_ThenTheCorrectRouteShouldBeRetrieved()
            throws IOException {
        assertThat(routeFinderService.routing("CZE", "ITA"))
                .isEqualTo(Arrays.asList("CZE", "AUT", "ITA"));
    }

    @Test
    void givenOneCountryWithNeighboursAndOneWithout_WhenProcessingRoute_ThenEmptyListShouldBeRetrieved()
            throws IOException {
        assertThat(routeFinderService.routing("USA", "MUS")).isEmpty();
    }

    @Test
    void givenOneValidAndOneInvalid_WhenProcessingRoute_ThenEmptyListShouldBeRetrieved() throws IOException {
        assertThat(routeFinderService.routing("CZE", "LLL")).isEmpty();
    }

    @Test
    void givenTwoCountriesWithoutNeighbours_WhenProcessingRoute_ThenEmptyListShouldBeRetrieved() throws IOException {
        assertThat(routeFinderService.routing("ATF", "AIA")).isEmpty();
    }

    @Test
    void givenTwoCountriesThatDoNotExist_WhenProcessingRoute_ThenEmptyListShouldBeRetrieved() throws IOException {
        assertThat(routeFinderService.routing("WQFWQFQ", "RYURNRN")).isEmpty();
    }
}
