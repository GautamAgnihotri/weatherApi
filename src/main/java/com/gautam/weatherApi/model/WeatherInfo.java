package com.gautam.weatherApi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class WeatherInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Location location;

    private LocalDate date;
    private Double temperature;
    private Double humidity;
    private String description;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}

