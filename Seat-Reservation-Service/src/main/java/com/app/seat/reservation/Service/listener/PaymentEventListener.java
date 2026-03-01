package com.app.seat.reservation.Service.listener;

import com.app.commons.constant.EventsConstant;
import com.app.commons.constant.PaymentStatus;
import com.app.commons.constant.SeatReservationStatus;
import com.app.commons.createdEvents.PaymentCreatedEvent;
import com.app.seat.reservation.Service.entity.SeatReservationEntity;
import com.app.seat.reservation.Service.repository.SeatReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    private Logger logger =
            LoggerFactory.getLogger(PaymentEventListener.class);

    private KafkaTemplate<String,Object> kafkaTemplate;

    private SeatReservationRepository seatReservationRepository;

    public PaymentEventListener(KafkaTemplate<String, Object> kafkaTemplate, SeatReservationRepository seatReservationRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.seatReservationRepository = seatReservationRepository;
    }

    @KafkaListener(topics = {EventsConstant.PAYMENT_EVENT_TOPIC},
            groupId = EventsConstant.RESERVATION_GRP)
    public void listenPaymentEvent(PaymentCreatedEvent paymentCreatedEvent) {

        if(paymentCreatedEvent.status().equals(PaymentStatus.FAILED)) {
            logger.info("payment failed........");

            paymentCreatedEvent.seatReservationId().forEach(seatReserveIds -> {
                SeatReservationEntity bySeatReservationId =
                        seatReservationRepository.findBySeatReservationId(seatReserveIds);

                //make the seat available for the new User
                bySeatReservationId.setStatus(SeatReservationStatus.AVAILABLE);
                // by making bookingId as -1.
                bySeatReservationId.setBookingId(-1);

                bySeatReservationId.setUserId(-1);

                seatReservationRepository.save(bySeatReservationId);

            });

            try {
                kafkaTemplate.send(EventsConstant.BOOKING_EVENT_TOPIC,
                        String.valueOf(paymentCreatedEvent.PaymentId())
                        , paymentCreatedEvent);
            } catch(Exception exception) {
                exception.printStackTrace();
                throw new RuntimeException("error while sending failed event" +
                        " back to the booking topic...");
            }
        } else {
            logger.info("payment successfully listening in seat reservation-service........");
        }

    }
}
