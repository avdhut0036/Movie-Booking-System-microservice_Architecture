package com.app.seat.reservation.Service.listener;

import com.app.commons.constant.EventsConstant;
import com.app.commons.constant.SeatReservationStatus;
import com.app.commons.createdEvents.BookingCreatedEvent;
import com.app.seat.reservation.Service.entity.SeatReservationEntity;
import com.app.seat.reservation.Service.events.SeatReservationSendEvent;
import com.app.seat.reservation.Service.repository.SeatReservationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SeatReservationListener {

    private SeatReservationRepository seatReservationRepository;

    private SeatReservationSendEvent seatReservationSendEvent;

    public SeatReservationListener(SeatReservationRepository seatReservationRepository, SeatReservationSendEvent seatReservationSendEvent) {
        this.seatReservationRepository = seatReservationRepository;
        this.seatReservationSendEvent = seatReservationSendEvent;
    }

    @KafkaListener(topics = {EventsConstant.BOOKING_EVENT_TOPIC},
            groupId = EventsConstant.RESERVATION_GRP)
    public void listenerSeatReservation(BookingCreatedEvent bookingCreatedEvent) {
            // by using the showId and seat number find the entity from DB
        // then set the status as booked set booking id
        // save that record so that seat is booked.
        // publish the event to the SEAT-RESERVATION-TOPIC.

        List<SeatReservationEntity> seatReservationEntities =
                bookingCreatedEvent.seatIds().stream().map(seatNo -> {
            SeatReservationEntity byShowIdAndSeatNumber =
                    seatReservationRepository.
                            findByShowIdAndSeatNumber(
                                    bookingCreatedEvent.showId(),
                                    seatNo);
            if(byShowIdAndSeatNumber.getBookingId() == -1 &&
            byShowIdAndSeatNumber.getUserId() == -1) {
                byShowIdAndSeatNumber.setStatus(SeatReservationStatus.BOOKED);
                byShowIdAndSeatNumber.setBookingId(bookingCreatedEvent.bookingId());
                byShowIdAndSeatNumber.setUserId(bookingCreatedEvent.userId());
            }
            return byShowIdAndSeatNumber;
        }).toList();

        seatReservationRepository.saveAll(seatReservationEntities);

        seatReservationSendEvent.sendSeatReservationEvent(seatReservationEntities,
                bookingCreatedEvent);


    }


}
