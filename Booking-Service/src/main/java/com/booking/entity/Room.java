package com.booking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Room {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false , length = 10)
	private Double pricePerNight;
	
	@Column(nullable = false , length = 10)
	private Integer noOfRooms;
	
	@ManyToOne
	@JoinColumn(nullable = false , name = "room_category_id")
	private RoomCategory roomCategory;
	
	@ManyToOne
	@JoinColumn(nullable = false , name = "property_id")
	private Property property;

}
