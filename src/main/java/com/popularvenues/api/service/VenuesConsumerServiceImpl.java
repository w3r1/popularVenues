package com.popularvenues.api.service;

import com.popularvenues.api.client.VenuesConsumerClient;
import com.popularvenues.api.domain.Venue;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class VenuesConsumerServiceImpl implements VenuesConsumerService {

    @Autowired
    private VenuesConsumerClient venuesConsumerClient;

    @Override
    public List<Venue> getPopularVenues(String near) {

        return venuesConsumerClient.getPopularVenues(near);
    }
}
