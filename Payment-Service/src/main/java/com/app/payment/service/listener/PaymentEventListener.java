package com.app.payment.service.listener;

import com.app.commons.constant.EventsConstant;
import com.app.commons.createdEvents.SeatReservationCreatedEvent;
import com.app.payment.service.events.PaymentSendingEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    private PaymentSendingEvent paymentSendingEvent;


    public PaymentEventListener(PaymentSendingEvent paymentSendingEvent) {
        this.paymentSendingEvent = paymentSendingEvent;
    }

    @KafkaListener(topics = {EventsConstant.RESERVATION_EVENT_TOPIC},groupId = EventsConstant.PAYMENT_GRP)
    public void paymentListener(SeatReservationCreatedEvent seatReservationCreatedEvent) {
        // if success based on condition send an successful event back
        // create an failure scenario and throw an exception based
        // 2 on that exception send an failure event back to Payment-topic

        if(seatReservationCreatedEvent.amount() > 2000) {
                paymentSendingEvent.sendPaymentEvent(false,seatReservationCreatedEvent);
        } else {
            paymentSendingEvent.sendPaymentEvent(true,seatReservationCreatedEvent);
        }

    }
}
