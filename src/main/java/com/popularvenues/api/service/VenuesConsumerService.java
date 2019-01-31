package com.popularvenues.api.service;

import com.popularvenues.api.domain.Venue;

import java.util.List;

public interface VenuesConsumerService {

    List<Venue> getPopularVenues(String near);
}
