package com.app.Booking.Service.repository;

import com.app.Booking.Service.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    BookingEntity findByBookingId(Long bookingId);
}
