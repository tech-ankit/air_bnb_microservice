package com.property.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.property.entity.Property;
import com.property.entity.PropertyImage;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {

	Optional<PropertyImage> findByProperty(Property property);

}
