package com.property.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Property {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String propertyId;
	
	@Column(nullable = false , length = 100 )
	private String propertyName;
	
	@Column(nullable = false)
	private String propertyAddress;
	
	@Column(nullable = false, length = 5)
	private Integer noOfBeds;
	
	@Column(nullable = false, length = 5)
	private Integer noOfRooms;
	
	@Column(nullable = false, length = 5)
	private Integer noOfBathrooms;
	
	@Column(nullable = false, length = 10)
	private Boolean isBookingStarted;
	
	@Column(nullable = false, length = 10)
	private Boolean isPetAllowed;
	
	@Column(nullable = false, length = 5)
	private Integer noOfGuest;
	
	@Column(length = 20)
	private Long viewCount;
	
	@Column(length = 20)
	private Long bookingCount;
	
	@Column(length = 2, nullable = false) 
	private Integer taxInPercentage;
	
	@Column(nullable = false , length = 20)
	private String userId;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime registerDate;
	
	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDateTime updateDate;
	
	@ManyToOne
	@JoinColumn(name = "property_category_id")
	private PropertyCategory propertyCategory;
	
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;
	
	@ManyToOne
	@JoinColumn(nullable = false , name = "city_id")
	private City city;
	
	@ManyToOne
	@JoinColumn(nullable = false , name = "location_id")
	private Location location;
	
	@ManyToOne
	@JoinColumn(nullable = false , name = "state_id")
	private State state;
}
