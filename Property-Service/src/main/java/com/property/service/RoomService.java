package com.property.service;

import com.property.entity.Room;

public interface RoomService {

	Room addRoom(String propertyId, Room room, Long roomCategoryId);
	String removeRoom(Long roomId);
	Room updateRoom(Long roomId, Room updatedRoom,Long categoryId);
	Room getRoomById(Long roomId);
}
