package com.app.commons.createdEvents;

import com.app.commons.constant.PaymentStatus;

import java.util.List;

/**
 * RECORDS ARE IMMUTABLE
 * IT ONLY HAS SETTERS DOESN'T HAVE GETTERS
 * IT WILL GIVE
 */
public record PaymentCreatedEvent(List<String> seatReservationId, long bookingId, int PaymentId, PaymentStatus status ) {
}
