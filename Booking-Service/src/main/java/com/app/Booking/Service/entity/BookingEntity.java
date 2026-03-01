package com.app.Booking.Service.entity;

import com.app.commons.constant.BookingStatus;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "booking")
public class BookingEntity {

    @Id
    private long bookingId;

    private int userId;

    @ElementCollection
    private List<String> seatIds;

    private int theaterId;

    private int showId; // ScreenId

    private BookingStatus status;


}
