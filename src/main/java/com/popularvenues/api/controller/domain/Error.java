package com.popularvenues.api.controller.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Error {

    @JsonProperty
    private int statusCode;

    @JsonProperty
    private String errorMessage;
}