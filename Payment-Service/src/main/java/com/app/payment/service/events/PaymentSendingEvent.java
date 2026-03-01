package com.app.payment.service.events;

import com.app.commons.constant.EventsConstant;
import com.app.commons.constant.PaymentStatus;
import com.app.commons.createdEvents.PaymentCreatedEvent;
import com.app.commons.createdEvents.SeatReservationCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PaymentSendingEvent {

    private KafkaTemplate<String,Object> kafkaTemplate;

    public PaymentSendingEvent(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPaymentEvent(Boolean isSuccess,
                                 SeatReservationCreatedEvent seatReservationCreatedEvent) {
        PaymentCreatedEvent paymentCreatedEvent =
                isSuccess ?
                        new PaymentCreatedEvent(seatReservationCreatedEvent.seatReservationId(),
                                seatReservationCreatedEvent.bookingId(),
                                new Random().nextInt(),
                                PaymentStatus.SUCCESSFUL)
                         :
                        new PaymentCreatedEvent(
                                seatReservationCreatedEvent.seatReservationId(),
                                seatReservationCreatedEvent.bookingId(),
                                -new Random().nextInt(),
                                PaymentStatus.FAILED);

        try {
            kafkaTemplate.send(EventsConstant.PAYMENT_EVENT_TOPIC,
                    String.valueOf(paymentCreatedEvent.PaymentId()), paymentCreatedEvent);
        } catch(Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("error while sending an payment-topic event.....!!!!!");
        }
    }
}
