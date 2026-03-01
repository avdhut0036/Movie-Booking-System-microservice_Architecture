package com.app.seat.reservation.Service.repository;

import com.app.seat.reservation.Service.entity.SeatReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatReservationRepository extends JpaRepository<SeatReservationEntity, Long> {
    SeatReservationEntity findByShowIdAndSeatNumber(int showId, String seatNo);

    SeatReservationEntity findBySeatReservationId(String seatReserveIds);
}
