package com.booking.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.booking.entity.Booking;
import com.booking.entity.Property;
import com.booking.entity.Room;
import com.booking.payload.User;

public interface BookingRepository extends JpaRepository<Booking, Long>{

	Optional<Booking> findByRoomId(Long roomId);

	@Query("SELECT b FROM Booking b WHERE "
			+ "b.roomId =:roomId "
			+ "AND b.checkInDate <=:checkoutDate "
			+ "AND b.checkoutDate >=:checkInDate ")
	List<Booking> getBookedRooms(Long roomId , LocalDate checkInDate,LocalDate checkoutDate);

	@Query("SELECT b FROM Booking b where b.propertyId=:propertyId")
	List<Booking> findByProperty(String property);

	List<Booking> findByUserId(String userId);

	@Query("SELECT b FROM Booking b WHERE b.bookingStatus = 'PENDING' AND b.expiry < :now")
	List<Booking> findExpiredPendingBookings(@Param("now") LocalDateTime now);


}
