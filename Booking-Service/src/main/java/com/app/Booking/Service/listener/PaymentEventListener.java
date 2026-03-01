package com.app.Booking.Service.listener;

import com.app.Booking.Service.services.BookingService;
import com.app.commons.constant.EventsConstant;
import com.app.commons.constant.PaymentStatus;
import com.app.commons.createdEvents.PaymentCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    private Logger logger =
            LoggerFactory.getLogger(PaymentEventListener.class);

    private BookingService bookingService;

    public PaymentEventListener(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @KafkaListener(topics = {EventsConstant.PAYMENT_EVENT_TOPIC},
    groupId = EventsConstant.booking_grp)
    public void listenPaymentEvent(PaymentCreatedEvent paymentCreatedEvent){

        if(paymentCreatedEvent.status().equals(PaymentStatus.SUCCESSFUL)) {
            logger.info("Payment is successful listening from booking-service");
            bookingService.successfulPaymentBooking(paymentCreatedEvent.bookingId());
        } else {
            logger.info("Payment failed... listening from booking-service");
            bookingService.failedPaymentRevertBooking(paymentCreatedEvent.bookingId());
        }

    }
}
