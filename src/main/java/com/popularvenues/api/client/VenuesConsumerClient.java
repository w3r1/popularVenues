package com.popularvenues.api.client;

import com.popularvenues.api.client.exception.ConsumerClientException;
import com.popularvenues.api.client.exception.ConsumerClientResponseEmptyException;
import com.popularvenues.api.domain.ClientResponse;
import com.popularvenues.api.domain.Response;
import com.popularvenues.api.domain.Venue;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.join;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class VenuesConsumerClient {
    private static final Logger LOGGER = Logger.getLogger(VenuesConsumerClient.class.getName());

    private static final String HTTPS = "https://";
    private static final String PORT_DELIMITER = ":";
    private static final String POPULAR_VENUES = "/v2/venues/trending";

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    private final String clientId;
    private final String clientSecret;
    private final String apiVersion;
    private final String popularVenuesUrl;

    @Autowired
    public VenuesConsumerClient(RestTemplateBuilder restTemplateBuilder,
                                @Value("${locations.server.host}") String serverHost,
                                @Value("${locations.server.port:}") String serverPort,
                                @Value("${locations.server.auth.clientid}") String clientId,
                                @Value("${locations.server.auth.clientsecret}") String clientSecret,
                                @Value("${locations.server.api.version}") String apiVersion) {

        this.restTemplate = restTemplateBuilder.build();
        this.headers = new HttpHeaders();
        this.headers.set("Accept", APPLICATION_JSON_VALUE);

        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.apiVersion = apiVersion;

        String path = HTTPS + serverHost + (isNotEmpty(serverPort) ? PORT_DELIMITER + serverPort : EMPTY);
        this.popularVenuesUrl = path + POPULAR_VENUES;
    }

    public List<Venue> getPopularVenues(String near) {

        UriComponentsBuilder popularVenuesUrlBuilder = UriComponentsBuilder.fromHttpUrl(popularVenuesUrl)
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("v", apiVersion)
                .queryParam("near", near);

        Optional<ResponseEntity<ClientResponse>> responseEntityOptional = Try.of(() -> restTemplate.exchange(
                popularVenuesUrlBuilder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                ClientResponse.class
        )).onFailure((e) -> {
            LOGGER.log(SEVERE, "Server not reachable: ", e);
            throw new ConsumerClientException("Location server not reachable, please try again later.");
        }).toJavaOptional();

        return responseEntityOptional
                .map(HttpEntity::getBody)
                .map(ClientResponse::getResponse)
                .filter(r -> r.getVenues().size() > 0)
                .map(Response::getVenues)
                .orElseThrow(() -> {
                    LOGGER.log(WARNING, join("No trending venues found for ", near));
                    return new ConsumerClientResponseEmptyException();
                });
    }
}