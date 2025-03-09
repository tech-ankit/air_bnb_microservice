package com.property.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.property.entity.State;

public interface StateRepository extends JpaRepository<State, Long> {

}
