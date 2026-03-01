package com.app.seat.reservation.Service.entity;

import com.app.commons.constant.SeatReservationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="seat_reservation")
public class SeatReservationEntity {

    @Id
    private Long seatReservationId;

    private long bookingId;  // only for reference

    private int showId;

    private String seatNumber;

    private int userId;

    private SeatReservationStatus status;



}
