package com.app.seat.reservation.Service.events;

import com.app.commons.constant.EventsConstant;
import com.app.commons.createdEvents.BookingCreatedEvent;
import com.app.commons.createdEvents.SeatReservationCreatedEvent;
import com.app.seat.reservation.Service.entity.SeatReservationEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SeatReservationSendEvent {

    private KafkaTemplate<String,Object> kafkaTemplate;

    public SeatReservationSendEvent(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendSeatReservationEvent(List<SeatReservationEntity> entities,
                                         BookingCreatedEvent bookingCreatedEvent) {


        List<String> reservationIdsList = entities.stream().map(
                SeatReservationEntity::getSeatNumber).toList();


        SeatReservationCreatedEvent seatReservationEvent =
                new SeatReservationCreatedEvent(
                        reservationIdsList,
                        bookingCreatedEvent.bookingId(),
                        bookingCreatedEvent.amount()
                );
        try {
            kafkaTemplate.send(EventsConstant.RESERVATION_EVENT_TOPIC,
                    String.valueOf(reservationIdsList.get(0)),
                    seatReservationEvent);
        } catch(Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("failed while sending event to reservation topic...!!!");
        }

    }
}
