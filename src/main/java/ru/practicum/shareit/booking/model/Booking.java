package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;
    @NonNull
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;
    @NonNull
    @Column(name = "item_id", nullable = false)
    private Long itemId;
    @NonNull
    @Column(name = "booker_id", nullable = false)
    private Long bookerId;
    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;
}
