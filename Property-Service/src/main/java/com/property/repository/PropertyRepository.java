package com.property.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.property.entity.Property;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

public interface PropertyRepository extends JpaRepository<Property, String>{

	List<Property> findByUserId(String ownerUserId);
	
	@Query("SELECT p FROM Property p "
		      + "JOIN p.country pc "
		      + "JOIN p.state ps "
		      + "JOIN p.city ppc "
		      + "JOIN p.location pl "
		      + "WHERE pc.countryName = :searchQuery OR ps.stateName = :searchQuery OR ppc.cityName = :searchQuery OR pl.locationName = :searchQuery")
	Page<Property> searchPropertyByQUery(@Param("searchQuery") String searchQuery, Pageable pageable);

	@Modifying
	@Transactional
	@Query("UPDATE Property p SET p.viewCount = p.viewCount + 1 WHERE p.propertyId =:propertyId ")
	void increaseViewCount(String propertyId);

}
