package com.popularvenues.api.service;

import com.popularvenues.api.client.VenuesConsumerClient;
import com.popularvenues.api.domain.PopularVenue;
import com.popularvenues.api.domain.Venue;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class VenuesConsumerServiceImpl implements VenuesConsumerService {

    @Autowired
    private VenuesConsumerClient venuesConsumerClient;

    @Override
    public List<PopularVenue> getPopularVenues(String near) {

        List<Venue> venues = venuesConsumerClient.getPopularVenues(near);
        return venues.stream()
                .filter(v -> v.getLocation() != null)
                .map(v -> PopularVenue.builder()
                                .name(v.getName())
                                .address(v.getLocation().getAddress())
                                .postalCode(v.getLocation().getPostalCode())
                                .city(v.getLocation().getCity())
                                .state(v.getLocation().getState())
                                .country(v.getLocation().getCountry())
                                .build())
                .collect(Collectors.toList());
    }
}
