package com.app.commons.createdEvents;

import java.util.List;

public record BookingCreatedEvent(int userId, int showId, List<String> seatIds, double amount, long bookingId) {
}
