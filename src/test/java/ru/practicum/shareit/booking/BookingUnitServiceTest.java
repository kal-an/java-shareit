package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.InvalidEntityException;
import ru.practicum.shareit.item.ItemNotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;
import ru.practicum.shareit.requests.ItemRequestRepository;
import ru.practicum.shareit.requests.RequestNotFoundException;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BookingUnitServiceTest {

    @Mock
    private ItemService itemService;
    @Mock
    private UserService userService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingService bookingService;

    private ItemDto itemDto;
    private UserDto userDto;
    private BookingCreationDto bookingCreationDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setName("John");
        userDto.setEmail("john@email.com");

        itemDto = new ItemDto();
        itemDto.setName("table");
        itemDto.setId(1L);
        itemDto.setDescription("red table");
        itemDto.setAvailable(true);
        itemDto.setOwnerId(1L);

        bookingCreationDto = new BookingCreationDto();
        bookingCreationDto.setItemId(1L);
        bookingCreationDto.setStart(LocalDateTime.now().plusHours(1));
        bookingCreationDto.setEnd(LocalDateTime.now().plusHours(1).plusSeconds(10));
    }

    @Test
    @DisplayName("Add booking user not found")
    void testAddBookingUserNotFound() {
        Mockito.when(userService.getUserById(anyLong()))
                .thenThrow(new UserNotFoundException("User not found"));
        BookingDto savedBookingDto = bookingService.addBooking(bookingCreationDto, 1L);
        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(anyLong()));
        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("Add booking item not found")
    void testAddBookingItemNotFound() {
        Mockito.when(itemService.getItemById(anyLong(), anyLong()))
                .thenThrow(new ItemNotFoundException("Item not found"));
        BookingDto savedBookingDto = bookingService.addBooking(bookingCreationDto, 1L);
        final ItemNotFoundException exception = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> itemService.getItemById(anyLong(), anyLong()));
        Assertions.assertEquals("Item not found", exception.getMessage());
    }

    @Test
    @DisplayName("Update booking not found")
    void testUpdateBookingNotFound() {
        Mockito.when(bookingRepository.findById(anyLong()))
                .thenThrow(new BookingNotFoundException("Booking with ID not found"));
        final BookingNotFoundException exception = Assertions.assertThrows(
                BookingNotFoundException.class,
                () -> bookingRepository.findById(anyLong()));
        Assertions.assertEquals("Booking with ID not found", exception.getMessage());
    }

    @Test
    @DisplayName("Get user bookings wrong request parameters")
    void testGetAllUserBookings() {
        Mockito.when(bookingService.getAllUserBookings(anyInt(), anyInt(), anyString(), anyLong()))
                .thenThrow(new InvalidEntityException("Invalid request parameter"));

        final InvalidEntityException exception = Assertions.assertThrows(InvalidEntityException.class,
                () -> bookingService.getAllUserBookings(anyInt(), anyInt(), anyString(), anyLong()));
        Assertions.assertEquals("Invalid request parameter", exception.getMessage());
    }

}
