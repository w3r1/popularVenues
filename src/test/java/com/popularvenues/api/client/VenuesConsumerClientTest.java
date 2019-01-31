package com.popularvenues.api.client;

import com.popularvenues.api.client.domain.*;
import com.popularvenues.api.exception.ConsumerClientException;
import com.popularvenues.api.exception.ConsumerClientResponseEmptyException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.join;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.OK;

public class VenuesConsumerClientTest {

    private static final String TEST_HOST = "host";
    private static final String TEST_PORT = "";
    private static final String TEST_CLIENT_ID = "clientId";
    private static final String TEST_CLIENT_SECRET = "clientSecret";
    private static final String TEST_API_VERSION = "apiVersion";

    @Rule
    public ExpectedException expectedException = none();

    private RestTemplateBuilder restTemplateBuilder;
    private RestTemplate restTemplate;

    private VenuesConsumerClient venuesConsumerClient;

    @Before
    public void setup() {

        restTemplateBuilder = mock(RestTemplateBuilder.class);
        restTemplate = mock(RestTemplate.class);
        given(restTemplateBuilder.build()).willReturn(restTemplate);

        venuesConsumerClient = new VenuesConsumerClient(
                this.restTemplateBuilder,
                TEST_HOST,
                TEST_PORT,
                TEST_CLIENT_ID,
                TEST_CLIENT_SECRET,
                TEST_API_VERSION
        );
    }

    @Test
    public void shouldGetPopularVenues() {

        String testNear = "London";
        ClientResponse testClientResponse = defaultClientResponse();

        ResponseEntity<ClientResponse> responseEntity
                = new ResponseEntity<>(testClientResponse, OK);
        given(restTemplate
                .exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .willReturn(responseEntity);

        List<Venue> popularVenues = venuesConsumerClient.getPopularVenues(testNear);
        verify(restTemplate, times(1))
                .exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class));
        assertThat(popularVenues, hasSize(2));
        assertThat(popularVenues, hasItems(testClientResponse.getResponse().getVenues().toArray(new Venue[2])));
    }

    @Test
    public void shouldBuildUriWithoutPortNumber() {

        String testPort = "8080";

        String testNear = "London";
        ClientResponse testClientResponse = defaultClientResponse();

        venuesConsumerClient = new VenuesConsumerClient(
                restTemplateBuilder,
                TEST_HOST,
                testPort,
                TEST_CLIENT_ID,
                TEST_CLIENT_SECRET,
                TEST_API_VERSION
        );

        ResponseEntity<ClientResponse> responseEntity
                = new ResponseEntity<>(testClientResponse, OK);
        ArgumentCaptor<String> uriCaptor = ArgumentCaptor.forClass(String.class);
        given(restTemplate
                .exchange(uriCaptor.capture(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .willReturn(responseEntity);

        venuesConsumerClient.getPopularVenues(testNear);
        verify(restTemplate,
                times(1)).exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class));

        String capturedUri = uriCaptor.getValue();
        assertThat(capturedUri, equalTo(
                join("https://",
                        TEST_HOST,
                        ":",
                        testPort,
                        "/v2/venues/trending?client_id=",
                        TEST_CLIENT_ID,
                        "&client_secret=",
                        TEST_CLIENT_SECRET,
                        "&v=",
                        TEST_API_VERSION,
                        "&near=",
                        testNear)
        ));
    }

    @Test
    public void shouldBuildUriWithPortNumber() {

        String testNear = "London";
        ClientResponse testClientResponse = defaultClientResponse();

        ResponseEntity<ClientResponse> responseEntity
                = new ResponseEntity<>(testClientResponse, OK);
        ArgumentCaptor<String> uriCaptor = ArgumentCaptor.forClass(String.class);
        given(restTemplate
                .exchange(uriCaptor.capture(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .willReturn(responseEntity);

        venuesConsumerClient.getPopularVenues(testNear);
        verify(restTemplate,
                times(1)).exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class));

        String capturedUri = uriCaptor.getValue();
        assertThat(capturedUri, equalTo(
                join("https://",
                        TEST_HOST,
                        "/v2/venues/trending?client_id=",
                        TEST_CLIENT_ID,
                        "&client_secret=",
                        TEST_CLIENT_SECRET,
                        "&v=",
                        TEST_API_VERSION,
                        "&near=",
                        testNear)
        ));
    }

    @Test
    public void shouldThrowConsumerClientExceptionWhenServerCallUnsuccessful() {

        expectedException.expect(ConsumerClientException.class);
        expectedException.expectMessage("Location server not reachable, please try again later.");

        String testNear = "London";
        ClientResponse testClientResponse = defaultClientResponse();

        ResponseEntity<ClientResponse> responseEntity
                = new ResponseEntity<>(testClientResponse, OK);
        given(restTemplate
                .exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .willAnswer(e -> { throw new Exception(); });

        venuesConsumerClient.getPopularVenues(testNear);
    }

    @Test
    public void shouldThrowConsumerClientResponseEmptyExceptionWhenNoResult() {

        expectedException.expect(ConsumerClientResponseEmptyException.class);

        String testNear = "London";
        ClientResponse testClientResponse = defaultClientResponse();
        testClientResponse.getResponse().getVenues().clear();

        ResponseEntity<ClientResponse> responseEntity
                = new ResponseEntity<>(testClientResponse, OK);
        given(restTemplate
                .exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .willReturn(responseEntity);

        venuesConsumerClient.getPopularVenues(testNear);
    }

    private ClientResponse defaultClientResponse() {

        Location location = Location.builder()
                .address("Web Street")
                .postalCode("W3C")
                .city("London")
                .state("Greater London")
                .country("UK")
                .build();
        Venue venue = Venue.builder()
                .name("Nice Place")
                .location(location)
                .build();

        Location location2 = Location.builder()
                .address("J Street")
                .postalCode("J4G 00D")
                .city("London")
                .state("Greater London")
                .country("UK")
                .build();
        Venue venue2 = Venue.builder()
                .name("Great Place")
                .location(location2)
                .build();

        Response response = Response.builder()
                .venues(new ArrayList<>(asList(venue, venue2)))
                .build();
        Meta meta = Meta.builder()
                .code(OK.toString())
                .build();

        return ClientResponse.builder()
                .meta(meta)
                .response(response)
                .build();
    }
}