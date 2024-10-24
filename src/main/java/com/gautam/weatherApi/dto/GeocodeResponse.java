package com.gautam.weatherApi.dto;


import lombok.Data;

@Data
public class GeocodeResponse {
    private Double lat;
    private Double lon;
    private String name;
    private String country;
    private String state;
}