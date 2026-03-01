package com.app.Booking.Service.controller;

import com.app.Booking.Service.entity.BookingEntity;
import com.app.Booking.Service.services.BookingService;
import com.app.commons.request.BookingRequest;
import com.app.commons.response.BookingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/book")
public class BookingController {

    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        BookingEntity bookingEntity = bookingService.createBooking(request);

        return ResponseEntity.ok(new BookingResponse(bookingEntity.getBookingId(),
                bookingEntity.getStatus()));
    }
}
