package com.popularvenues.api.client.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {

    @JsonProperty("meta")
    private Meta meta;

    @JsonProperty("response")
    private Response response;
}
