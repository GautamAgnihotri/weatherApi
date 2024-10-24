package com.gautam.weatherApi.dto;

import lombok.Data;
import java.time.LocalDate;

import lombok.Data;
import java.util.List;

@Data
public class WeatherApiResponse {
    private Main main;
    private List<Weather> weather;

    @Data
    public static class Main {
        private Double temp;
        private Double humidity;
    }

    @Data
    public static class Weather {
        private String description;
    }
}
