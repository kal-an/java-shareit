package ru.practicum.shareit.booking.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingNotFoundException;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.InvalidEntityException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserService userService,
                              ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public BookingDto addBooking(BookingShortDto bookingDto, long bookerId) {
        final UserDto bookerDto = userService.getUserById(bookerId);
        final ItemDto itemDto = itemService.getItemById(bookingDto.getItemId());
        final Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setItemId(itemDto.getId());
        booking.setBookerId(bookerId);
        booking.setStatus(Status.WAITING);
        if (!itemDto.getAvailable()) {
            log.error("Item with ID {} not available for booking", itemDto.getId());
            throw new InvalidEntityException("Item not available for booking");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            log.error("Date not valid for booking");
            throw new InvalidEntityException("Date not valid for booking");
        }
        if (itemDto.getOwnerId().equals(bookerId)) {
            log.error("User ID {} could not booking own item ID {}", bookerId, itemDto.getId());
            throw new BookingNotFoundException("User could not booking own item");
        }
        final Booking savedBookingInDb = bookingRepository.save(booking);
        log.info("Booking {} saved", savedBookingInDb);
        final BookingDto convertToDto = BookingMapper.toBookingDto(savedBookingInDb);
        convertToDto.setItem(itemDto);
        convertToDto.setBooker(bookerDto);
        return convertToDto;
    }

    @Override
    public BookingDto updateBooking(long bookingId,
                                    long ownerId,
                                    boolean approved) {
        final Booking bookingInDb = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException(String.format("Booking with ID %d not found", bookingId)));
        userService.getUserById(ownerId);
        final ItemDto itemDto = itemService.getItemById(bookingInDb.getItemId());
        final UserDto bookerDto = userService.getUserById(bookingInDb.getBookerId());
        if (!itemDto.getOwnerId().equals(ownerId)) {
            log.error("UserID not equal item owner");
            throw new BookingNotFoundException(String.format("Booking with ID %d not found", bookingId));
        }
        if (approved && bookingInDb.getStatus().equals(Status.REJECTED)) {
            log.error("Booking ID {} already rejected", bookingId);
            throw new InvalidEntityException("Booking already rejected");
        }
        if (approved && bookingInDb.getStatus().equals(Status.APPROVED)) {
            log.error("Booking ID {} already approved", bookingId);
            throw new InvalidEntityException("Booking already approved");
        }
        bookingInDb.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        final Booking updateBookingInDb = bookingRepository.save(bookingInDb);
        log.info("Booking {} updated", updateBookingInDb);
        final BookingDto convertToDto = BookingMapper.toBookingDto(updateBookingInDb);
        convertToDto.setItem(itemDto);
        convertToDto.setBooker(bookerDto);
        return convertToDto;
    }

    @Override
    public BookingDto getBookingById(long id, long userId) {
        userService.getUserById(userId);
        final Booking bookingInDb = bookingRepository.findById(id).orElseThrow(() ->
                new BookingNotFoundException(String.format("Booking with ID %d not found", id)));
        final BookingDto convertToDto = BookingMapper.toBookingDto(bookingInDb);
        final ItemDto itemDto = itemService.getItemById(bookingInDb.getItemId());
        final UserDto bookerDto = userService.getUserById(bookingInDb.getBookerId());
        if (!bookingInDb.getBookerId().equals(userId) && !itemDto.getOwnerId().equals(userId)) {
            log.error("BookingID {} not found for UserID {}", id, userId);
            throw new BookingNotFoundException(String.format("Booking with ID %d not found", id));
        }
        convertToDto.setItem(itemDto);
        convertToDto.setBooker(bookerDto);
        return convertToDto;
    }

    @Override
    public List<BookingDto> getAllUserBookings(String state, long userId) {
        userService.getUserById(userId);
        List<Booking> bookings;
        Sort sort = Sort.by(Sort.Direction.DESC, "end");
        switch (State.valueOf(state)) {
            case PAST:
                bookings = bookingRepository.findByBookerIdAndEndIsBefore(userId,
                        LocalDateTime.now(), sort);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now(), sort);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartIsAfter(userId,
                        LocalDateTime.now(), sort);
                break;
            case WAITING:
            case REJECTED:
                bookings = bookingRepository.findByBookerIdAndStatus(userId,
                        Status.valueOf(state), sort);
                break;
            default:
                bookings = bookingRepository.findByBookerId(userId, sort);
        }
        return convertToListDto(bookings);
    }

    @Override
    public List<BookingDto> getAllOwnerBookings(String state, long ownerId) {
        userService.getUserById(ownerId);
        List<Long> itemList = itemService.getAllOwnerItems(ownerId).stream()
                .map(ItemDto::getId)
                .collect(Collectors.toList());
        if (itemList.isEmpty()) {
            log.error("Owner with ID {} has no items", ownerId);
            throw new BookingNotFoundException("Owner has no items");
        }
        List<Booking> bookings;
        Sort sort = Sort.by(Sort.Direction.DESC, "end");
        switch (State.valueOf(state)) {
            case PAST:
                bookings = bookingRepository.findByItemIdInAndEndIsBefore(itemList,
                        LocalDateTime.now(), sort);
                break;
            case CURRENT:
                bookings = bookingRepository.findByItemIdInAndStartIsBeforeAndEndIsAfter(itemList,
                        LocalDateTime.now(), LocalDateTime.now(), sort);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemIdInAndStartIsAfter(itemList,
                        LocalDateTime.now(), sort);
                break;
            case WAITING:
            case REJECTED:
                bookings = bookingRepository.findByItemIdInAndStatus(itemList,
                        Status.valueOf(state), sort);
                break;
            default:
                bookings = bookingRepository.findByItemIdIn(itemList, sort);
        }
        return convertToListDto(bookings);
    }

    private List<BookingDto> convertToListDto(List<Booking> bookings) {
        final List<BookingDto> bookingDtoList = new ArrayList<>();
        for (Booking booking : bookings) {
            final BookingDto convertToDto = BookingMapper.toBookingDto(booking);
            final ItemDto itemDto = itemService.getItemById(booking.getItemId());
            final UserDto bookerDto = userService.getUserById(booking.getBookerId());
            convertToDto.setItem(itemDto);
            convertToDto.setBooker(bookerDto);
            bookingDtoList.add(convertToDto);
        }
        return bookingDtoList;
    }
}
