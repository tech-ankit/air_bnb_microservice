package com.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.property.entity.RoomCategory;

public interface RoomCategoryRepository extends JpaRepository<RoomCategory, Long> {

}
