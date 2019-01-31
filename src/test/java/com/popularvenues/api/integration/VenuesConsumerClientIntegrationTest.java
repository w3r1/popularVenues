package com.popularvenues.api.integration;

import com.popularvenues.api.client.VenuesConsumerClient;
import com.popularvenues.api.client.exception.ConsumerClientException;
import com.popularvenues.api.client.domain.Venue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VenuesConsumerClientIntegrationTest {

    @Rule
    public ExpectedException expectedException = none();

    @Autowired
    private VenuesConsumerClient venuesConsumerClient;

    @Test
    public void askForNearAndGetAnyVenueListBackWithoutExceptions() {

        String testNear = "Istanbul";
        List<Venue> popularVenues = venuesConsumerClient.getPopularVenues(testNear);
        assertThat(popularVenues.size(), greaterThan(0));
    }

    @Test
    public void askForNullAndGetPlannedException() {

        expectedException.expect(ConsumerClientException.class);
        expectedException.expectMessage("Location server not reachable, please try again later.");

        String testNear = null;
        List<Venue> popularVenues = venuesConsumerClient.getPopularVenues(testNear);
    }
}