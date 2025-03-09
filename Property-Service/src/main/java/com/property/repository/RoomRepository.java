package com.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.property.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
