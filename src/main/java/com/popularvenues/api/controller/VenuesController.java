package com.popularvenues.api.controller;

import com.popularvenues.api.controller.domain.VenuesResponse;
import com.popularvenues.api.domain.PopularVenue;
import com.popularvenues.api.service.VenuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/v1/venues")
public class VenuesController {

    @Autowired
    private VenuesService venuesService;

    @RequestMapping(value = "/popular", method = GET)
    public ResponseEntity<VenuesResponse> popular(@RequestParam String near) {

        List<PopularVenue> popularVenues = venuesService.getPopularVenues(near);

        return new ResponseEntity<>(new VenuesResponse(popularVenues), OK);
    }
}