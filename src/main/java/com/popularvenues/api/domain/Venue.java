package com.popularvenues.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Venue {

    @JsonProperty("name")
    private String name;

    @JsonProperty("location")
    private Location location;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return Objects.equals(name, venue.name) &&
                Objects.equals(location, venue.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location);
    }
}
