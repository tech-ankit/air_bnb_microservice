package com.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.property.entity.PropertyCategory;

public interface PropertyCategoryRepository extends JpaRepository<PropertyCategory, Long> {

}
