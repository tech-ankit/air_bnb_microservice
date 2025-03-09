package com.property.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.property.entity.Amenity;
import com.property.entity.PropertyAmenity;


public interface PropertyAmenityRepository extends JpaRepository<PropertyAmenity, Long> {

	@Query("SELECT p.amenity FROM PropertyAmenity p join p.property pp where pp.propertyId=:propertyId")
	List<Amenity> findByProperty(String propertyId);

}
