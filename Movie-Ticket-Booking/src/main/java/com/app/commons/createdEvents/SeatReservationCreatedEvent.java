package com.app.commons.createdEvents;

import java.util.List;

public record SeatReservationCreatedEvent(List<String> seatReservationId, long bookingId, double amount) {
}
