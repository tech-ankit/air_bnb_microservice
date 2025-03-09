package com.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.property.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

}
