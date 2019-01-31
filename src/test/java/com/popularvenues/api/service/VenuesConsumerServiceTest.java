package com.popularvenues.api.service;

import com.popularvenues.api.client.VenuesConsumerClient;
import com.popularvenues.api.domain.Venue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class VenuesConsumerServiceTest {

    @Mock
    private VenuesConsumerClient venuesConsumerClient;

    @InjectMocks
    private VenuesConsumerServiceImpl venuesConsumerService;

    @Test
    public void shouldGetPopularVenues() {

        String testNear = "London";

        String testVenueName = "testVenue";
        given(venuesConsumerClient.getPopularVenues(eq(testNear)))
                .willReturn(asList(new Venue(testVenueName, null)));

        List<Venue> popularVenues = venuesConsumerService.getPopularVenues(testNear);
        assertThat(popularVenues, hasItem(hasProperty("name", equalTo(testVenueName))));
    }
}