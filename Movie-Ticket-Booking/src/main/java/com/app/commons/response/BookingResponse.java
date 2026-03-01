package com.app.commons.response;

import com.app.commons.constant.BookingStatus;

public record BookingResponse(long bookingId, BookingStatus status) {
}
