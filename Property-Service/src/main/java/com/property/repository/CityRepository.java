package com.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.property.entity.City;

public interface CityRepository extends JpaRepository<City, Long> {

}
