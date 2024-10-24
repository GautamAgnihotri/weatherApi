package com.gautam.weatherApi.service;


import com.gautam.weatherApi.dto.GeocodeResponse;
import com.gautam.weatherApi.dto.WeatherApiResponse;
import com.gautam.weatherApi.exceptions.WeatherApiException;
import com.gautam.weatherApi.model.Location;
import com.gautam.weatherApi.model.WeatherInfo;
import com.gautam.weatherApi.repository.LocationRepository;
import com.gautam.weatherApi.repository.WeatherInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final LocationRepository locationRepository;
    private final WeatherInfoRepository weatherInfoRepository;
    private final RestTemplate restTemplate;

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String weatherApiUrl;

    @Value("${openweather.geocoding.url}")
    private String geocodingApiUrl;

    public WeatherService(LocationRepository locationRepository,
                          WeatherInfoRepository weatherInfoRepository,
                          RestTemplate restTemplate) {
        this.locationRepository = locationRepository;
        this.weatherInfoRepository = weatherInfoRepository;
        this.restTemplate = restTemplate;
    }

    public WeatherInfo getWeatherInfo(String pincode, LocalDate date) {
        logger.info("Fetching weather info for pincode: {} and date: {}", pincode, date);

        // Check cache first
        Optional<WeatherInfo> cachedInfo = weatherInfoRepository.findByLocationPincodeAndDate(pincode, date);
        if (cachedInfo.isPresent() && isDataFresh(cachedInfo.get())) {
            logger.info("Returning cached weather info for pincode: {}", pincode);
            return cachedInfo.get();
        }

        // Get or create location
        Location location = getOrCreateLocation(pincode);

        // Fetch new weather data
        WeatherInfo weatherInfo = fetchWeatherFromApi(location, date);

        // Save and return
        logger.info("Saving new weather info for pincode: {}", pincode);
        return weatherInfoRepository.save(weatherInfo);
    }

    private Location getOrCreateLocation(String pincode) {
        return locationRepository.findByPincode(pincode)
                .orElseGet(() -> createLocation(pincode));
    }

    private Location createLocation(String pincode) {
        logger.info("Creating new location for pincode: {}", pincode);
        try {
            // Call geocoding API
            String url = String.format("%szip?zip=%s,IN&limit=1&appid=%s",
                    geocodingApiUrl, pincode, apiKey);

            GeocodeResponse response = restTemplate.getForObject(url, GeocodeResponse.class);

            if (response == null) {
                throw new WeatherApiException("Failed to get location data for pincode: " + pincode);
            }

            Location location = new Location();
            location.setPincode(pincode);
            location.setLatitude(response.getLat());
            location.setLongitude(response.getLon());

            return locationRepository.save(location);

        } catch (Exception e) {
            logger.error("Error creating location for pincode: {}", pincode, e);
            throw new WeatherApiException("Error getting location data: " + e.getMessage());
        }
    }

    private WeatherInfo fetchWeatherFromApi(Location location, LocalDate date) {
        logger.info("Fetching weather data for location: {}", location.getPincode());
        try {
            String url = String.format("%sweather?lat=%s&lon=%s&appid=%s",
                    weatherApiUrl,
                    location.getLatitude(),
                    location.getLongitude(),
                    apiKey);

            WeatherApiResponse response = restTemplate.getForObject(url, WeatherApiResponse.class);

            if (response == null) {
                throw new WeatherApiException("Failed to get weather data");
            }

            WeatherInfo weatherInfo = new WeatherInfo();
            weatherInfo.setLocation(location);
            weatherInfo.setDate(date);
            weatherInfo.setTemperature(response.getMain().getTemp());
            weatherInfo.setHumidity(response.getMain().getHumidity());
            weatherInfo.setDescription(response.getWeather().get(0).getDescription());
            weatherInfo.setLastUpdated(LocalDateTime.now());

            return weatherInfo;

        } catch (Exception e) {
            logger.error("Error fetching weather data for location: {}", location.getPincode(), e);
            throw new WeatherApiException("Error fetching weather data: " + e.getMessage());
        }
    }

    private boolean isDataFresh(WeatherInfo weatherInfo) {
        return weatherInfo.getLastUpdated().plusHours(1).isAfter(LocalDateTime.now());
    }
}