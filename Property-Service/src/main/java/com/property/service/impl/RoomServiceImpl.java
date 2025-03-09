package com.property.service.impl;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.property.entity.Property;
import com.property.entity.Room;
import com.property.entity.RoomCategory;
import com.property.repository.PropertyRepository;
import com.property.repository.RoomCategoryRepository;
import com.property.repository.RoomRepository;
import com.property.service.RoomService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService{
	
	private final RoomCategoryRepository roomCategoryRepository;
	private final RoomRepository roomRepository;
	private final PropertyRepository propertyRepository;
	
	@Override
	public Room addRoom(String propertyId, Room room, Long roomCategoryId) {
		Optional<Property> byId = propertyRepository.findById(propertyId);
		Optional<RoomCategory> roomCategory = roomCategoryRepository.findById(roomCategoryId);
		if(byId.isPresent() && roomCategory.isPresent()) {
			room.setProperty(byId.get());
			room.setRoomCategory(roomCategory.get());
			return roomRepository.save(room);
		}
		return null;
	}

	@Override
	public String removeRoom(Long roomId) {
		Optional<Room> byId = roomRepository.findById(roomId);
		if(byId.isPresent()) {
			Room room = byId.get();
			roomRepository.delete(room);
			Optional<Room> checkRoomById = roomRepository.findById(roomId);
			if(checkRoomById.isEmpty()) {
				return "Room Deleted";
			}
		}
		return null;
	}

	@Override
	public Room updateRoom(Long roomId, Room updatedRoom,Long categoryId) {
		Optional<Room> byId = roomRepository.findById(roomId);
		Optional<RoomCategory> roomCategory = roomCategoryRepository.findById(categoryId);
		if(byId.isPresent()) {
			Room room = byId.get();
			room.setNoOfRooms(updatedRoom.getNoOfRooms());
			room.setPricePerNight(updatedRoom.getPricePerNight());
			if(roomCategory.isPresent()) {
				room.setRoomCategory(roomCategory.get());
			}
			return roomRepository.save(room);
		}
		return null;
	}

	@Override
	public Room getRoomById(Long roomId) {
		Optional<Room> byId = roomRepository.findById(roomId);
		if(byId.isPresent()) {
			return byId.get();
		}
		return null;
	}

}
