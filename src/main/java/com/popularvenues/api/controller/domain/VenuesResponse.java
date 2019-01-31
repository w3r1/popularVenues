package com.popularvenues.api.controller.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.popularvenues.api.domain.PopularVenue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class VenuesResponse {

    @JsonProperty("venues")
    private List<PopularVenue> popularVenues;
}
