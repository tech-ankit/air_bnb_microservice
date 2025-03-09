package com.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.property.entity.Amenity;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {

}
