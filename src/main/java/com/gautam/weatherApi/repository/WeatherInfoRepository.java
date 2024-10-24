package com.gautam.weatherApi.repository;

import com.gautam.weatherApi.model.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WeatherInfoRepository extends JpaRepository<WeatherInfo, Long>, JpaSpecificationExecutor<WeatherInfo> {

    Optional<WeatherInfo> findByLocationPincodeAndDate(String pincode, LocalDate date);

}
