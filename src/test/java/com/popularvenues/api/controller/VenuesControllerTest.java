package com.popularvenues.api.controller;

import com.popularvenues.api.controller.domain.VenuesResponse;
import com.popularvenues.api.domain.PopularVenue;
import com.popularvenues.api.service.VenuesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;

@RunWith(MockitoJUnitRunner.class)
public class VenuesControllerTest {

    @Mock
    private VenuesService venuesService;

    @InjectMocks
    private VenuesController venuesController;

    @Test
    public void shouldGetPopularVenues() {

        String testNear = "London";

        String testVenueName = "name";
        String testVenueAddress = "address";
        given(venuesService.getPopularVenues(eq(testNear)))
            .willReturn(asList(PopularVenue.builder().name(testVenueName).address(testVenueAddress).build()));

        ResponseEntity<VenuesResponse> response = venuesController.popular(testNear);
        assertThat(response.getStatusCode(), equalTo(OK));
        assertThat(response.getBody().getPopularVenues(), hasItem(hasProperty("name", equalTo(testVenueName))));
        assertThat(response.getBody().getPopularVenues(), hasItem(hasProperty("address", equalTo(testVenueAddress))));
    }
}