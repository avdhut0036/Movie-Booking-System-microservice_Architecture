package com.app.Booking.Service.mapper;

import com.app.Booking.Service.entity.BookingEntity;
import com.app.commons.constant.BookingStatus;
import com.app.commons.request.BookingRequest;

import java.util.Random;

public class ConvertBookingRequestToEntity {

    public static BookingEntity convertToEntity(BookingRequest request) {
        return
                BookingEntity.builder()
                        .bookingId(new Random().nextLong())
                        .userId(request.userId())
                        .seatIds(request.seatIds())
                        .theaterId(request.theatreId())
                        .showId(request.showId())
                        .status(BookingStatus.PENDING)
                        .build();
    }
}
