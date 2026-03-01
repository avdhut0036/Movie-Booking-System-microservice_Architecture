package com.app.Booking.Service.events;

import com.app.Booking.Service.entity.BookingEntity;
import com.app.commons.constant.EventsConstant;
import com.app.commons.createdEvents.BookingCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookingEvents {

    private KafkaTemplate<String,Object> kafkaTemplate;

    public BookingEvents(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void SendBookingCreatedEvent(BookingEntity entity, double amount) {
        BookingCreatedEvent bookingCreatedEvent =
                new BookingCreatedEvent(entity.getUserId(),entity.getShowId(),
                        entity.getSeatIds(), amount, entity.getBookingId());
        try {
            kafkaTemplate.send(EventsConstant.BOOKING_EVENT_TOPIC,
                    String.valueOf(entity.getBookingId()), bookingCreatedEvent);
        }catch (Exception exception){
            exception.printStackTrace();
            throw new RuntimeException("Failed to send an event in booking topic....");
        }
    }
}
