package com.gautam.weatherApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class OpenWeatherResponse {
    private Main main;
    private List<Weather> weather;

    @Data
    public static class Main {
        private Double temp;
        private Double humidity;
        private Double pressure;
        @JsonProperty("feels_like")
        private Double feelsLike;
    }

    @Data
    public static class Weather {
        private String main;
        private String description;
    }
}
