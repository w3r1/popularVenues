package com.popularvenues.api.service;

import com.popularvenues.api.domain.PopularVenue;

import java.util.List;

public interface VenuesConsumerService {

    List<PopularVenue> getPopularVenues(String near);
}
