package com.app.commons.request;

import java.util.List;

public record BookingRequest(long bookingId, int showId, int userId, List<String> seatIds, int theatreId, double amount) {
}
