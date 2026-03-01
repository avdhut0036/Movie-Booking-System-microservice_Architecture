package com.app.Booking.Service.services;

import com.app.Booking.Service.entity.BookingEntity;
import com.app.Booking.Service.events.BookingEvents;
import com.app.Booking.Service.mapper.ConvertBookingRequestToEntity;
import com.app.Booking.Service.repository.BookingRepository;
import com.app.commons.constant.BookingStatus;
import com.app.commons.request.BookingRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BookingService {
    private BookingRepository bookingRepository;

    private BookingEvents bookingEvents;

    public BookingService(BookingRepository bookingRepository, BookingEvents bookingEvents) {
        this.bookingRepository = bookingRepository;
        this.bookingEvents = bookingEvents;
    }

    public BookingEntity createBooking(BookingRequest request) {

        // convert request to the booking entity object
        // we need to save the booking object
        // publish an event to booking-topic
        BookingEntity bookingEntity = bookingRepository.findByBookingId(request.bookingId());

        if(Objects.nonNull(bookingEntity)) { // if it's existing booking then only return the existing data.
            return bookingEntity;
        } else { // if it's new booking then go ahead with that processing.

            BookingEntity saveBookingEntity =
                    bookingRepository.save(ConvertBookingRequestToEntity.convertToEntity(request));

            bookingEvents.SendBookingCreatedEvent(saveBookingEntity, request.amount());

            return saveBookingEntity;
        }
    }

    public void failedPaymentRevertBooking(Long bookingId) {
        BookingEntity byBookingId = bookingRepository.findByBookingId(bookingId);

        byBookingId.setStatus(BookingStatus.FAILED);

        bookingRepository.save(byBookingId);
    }

    public void successfulPaymentBooking(Long bookingId) {
        BookingEntity byBookingId = bookingRepository.findByBookingId(bookingId);

        byBookingId.setStatus(BookingStatus.CONFIRMED);

        bookingRepository.save(byBookingId);
    }
}
