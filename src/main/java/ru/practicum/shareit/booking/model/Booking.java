package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @NotNull(message = "Item should not be null")
    @OneToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @NotNull(message = "Booker should not be null")
    @OneToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
}
