package com.property.rest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.property.entity.Room;
import com.property.service.RoomService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/room")
@AllArgsConstructor
public class RoomRestController {
	
	private final RoomService roomService;
	
	@PostMapping(value = "/add-room")
	public Room addRoom(@RequestParam String propertyId,@RequestBody Room room,@RequestParam Long roomCategoryId) {
		return roomService.addRoom(propertyId, room, roomCategoryId);
	}
	
	@DeleteMapping(value = "/delete/{roomId}")
	public String removeRoom(@PathVariable Long roomId) {
		return roomService.removeRoom(roomId);
	}
	
	@PutMapping(value = "/update/{roomId}")
	public Room updateRoom(@PathVariable Long roomId,@RequestBody Room updatedRoom,@RequestParam Long categoryId) {
		return roomService.updateRoom(roomId, updatedRoom, categoryId);
	}
	
	@GetMapping(value = "/by-id")
	public Room updateRoom(@RequestParam Long roomId) {
		return roomService.getRoomById(roomId);
	}

}
