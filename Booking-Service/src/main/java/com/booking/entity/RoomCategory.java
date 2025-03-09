package com.booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RoomCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roomCategoryId;
	
	@Column(nullable = false , unique = true , length = 20)
	private String roomCategoryName;

}
