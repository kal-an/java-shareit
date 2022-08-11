package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class ItemServiceTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Test
    void getAllOwnerItems() {

        UserDto userDto = new UserDto();
        userDto.setName("John");
        userDto.setEmail("john@email.com");

        UserDto bookerDto1 = new UserDto();
        bookerDto1.setName("Scott");
        bookerDto1.setEmail("scott@email.com");

        UserDto savedUserDto = userService.createUser(userDto);
        UserDto savedBookerDto1 = userService.createUser(bookerDto1);

        ItemDto itemDto = ItemDto.builder()
                .name("table")
                .description("white table")
                .available(true)
                .build();
        itemDto.setOwnerId(savedUserDto.getId());

        ItemDto savedItemDto = itemService.addItem(itemDto, savedUserDto.getId());

        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1).plusSeconds(3);

        BookingCreationDto bookingDto1 = new BookingCreationDto(
                start,
                end,
                savedItemDto.getId());
        BookingDto savedBookingDto1 = bookingService.addBooking(bookingDto1, savedBookerDto1.getId());

        BookingDto updatedBookingDto1 = bookingService.updateBooking(savedBookingDto1.getId(),
                savedUserDto.getId(), true);

        List<ItemDtoExtended> items = itemService.getAllOwnerItems(0, 10, savedUserDto.getId());

        assertThat(items, hasItem(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("name", equalTo(savedItemDto.getName())),
                hasProperty("description", equalTo(savedItemDto.getDescription())),
                hasProperty("available", equalTo(savedItemDto.getAvailable())),
                hasProperty("nextBooking", notNullValue()))));
    }
}
