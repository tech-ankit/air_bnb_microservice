package com.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.property.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
