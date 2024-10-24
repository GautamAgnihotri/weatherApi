package com.gautam.weatherApi.controller;

import com.gautam.weatherApi.model.WeatherInfo;
import com.gautam.weatherApi.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{pincode}")
    public ResponseEntity<WeatherInfo> getWeather(
            @PathVariable String pincode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate forDate) {
        WeatherInfo weatherInfo = weatherService.getWeatherInfo(pincode, forDate);
        return ResponseEntity.ok(weatherInfo);
    }
}