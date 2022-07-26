package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdAndEndIsBefore(long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(long bookerId, LocalDateTime start,
                                                              LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndStartIsAfter(long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findByBookerIdAndStatus(long bookerId, Status status, Sort sort);

    List<Booking> findByBookerId(long bookerId, Sort sort);

    List<Booking> findByItemIdInAndEndIsBefore(List<Long> itemIds, LocalDateTime end, Sort sort);

    List<Booking> findByItemIdInAndStartIsBeforeAndEndIsAfter(List<Long> itemIds,
                                                              LocalDateTime start,
                                                              LocalDateTime end, Sort sort);

    List<Booking> findByItemIdInAndStartIsAfter(List<Long> itemIds,
                                                LocalDateTime start, Sort sort);

    List<Booking> findByItemIdInAndStatus(List<Long> itemIds, Status status, Sort sort);

    List<Booking> findByItemIdIn(List<Long> itemIds, Sort sort);

    List<Booking> findByItemIdOrderByEndDesc(long itemId);

    List<Booking> findByItemIdAndBookerIdAndStatusAndEndIsBefore(long itemId, long bookerId,
                                                   Status status, LocalDateTime now);
}
