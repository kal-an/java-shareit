package ru.practicum.shareit.booking.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Start date should not be null")
    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime start;

    @NotNull(message = "End date should not be null")
    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime end;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @NotNull(message = "Booker ID should not be null")
    @Column(name = "booker_id", nullable = false)
    private Long bookerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
}
