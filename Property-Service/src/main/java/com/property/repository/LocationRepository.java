package com.property.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.property.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
