package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdAndEndIsBefore(long bookerId, LocalDateTime end,
                                               Pageable pageable);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(long bookerId,
                                                              LocalDateTime start,
                                                              LocalDateTime end,
                                                              Pageable pageable);

    List<Booking> findByBookerIdAndStartIsAfter(long bookerId,
                                                LocalDateTime start,
                                                Pageable pageable);

    List<Booking> findByBookerIdAndStatus(long bookerId, Status status, Pageable pageable);

    List<Booking> findByBookerId(long bookerId, Pageable pageable);

    List<Booking> findByItemIdInAndEndIsBefore(List<Long> itemIds,
                                               LocalDateTime end,
                                               Pageable pageable);

    List<Booking> findByItemIdInAndStartIsBeforeAndEndIsAfter(List<Long> itemIds,
                                                              LocalDateTime start,
                                                              LocalDateTime end,
                                                              Pageable pageable);

    List<Booking> findByItemIdInAndStartIsAfter(List<Long> itemIds,
                                                LocalDateTime start,
                                                Pageable pageable);

    List<Booking> findByItemIdInAndStatus(List<Long> itemIds, Status status, Pageable pageable);

    List<Booking> findByItemIdIn(List<Long> itemIds, Pageable pageable);

    List<Booking> findByItemIdOrderByEndDesc(long itemId);

    List<Booking> findByItemIdAndBookerIdAndStatusAndEndIsBefore(long itemId, long bookerId,
                                                   Status status, LocalDateTime now);
}
