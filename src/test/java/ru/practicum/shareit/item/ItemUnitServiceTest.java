package ru.practicum.shareit.item;

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
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.InvalidEntityException;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;
import ru.practicum.shareit.requests.ItemRequestRepository;
import ru.practicum.shareit.requests.RequestNotFoundException;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ItemUnitServiceTest {

    @Mock
    private ItemService itemService;
    @Mock
    private UserService userService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private BookingRepository bookingRepository;

    private ItemDto itemDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setName("John");
        userDto.setEmail("john@email.com");

        itemDto = new ItemDto();
        itemDto.setName("table");
        itemDto.setDescription("red table");
        itemDto.setAvailable(false);
        itemDto.setOwnerId(1L);
    }

    @Test
    @DisplayName("Create item with not found request exception")
    void testCreateItemNotFoundRequest() {
        itemDto.setRequestId(2L);
        Mockito.when(itemRequestRepository.findById(anyLong()))
                .thenThrow(new RequestNotFoundException("Request ID 2 not found"));
        Mockito.when(itemService.addItem(itemDto, 1L))
                .thenAnswer(invocationOnMock -> itemRequestRepository.findById(anyLong()));

        final RequestNotFoundException exception = Assertions.assertThrows(
                RequestNotFoundException.class,
                () -> itemService.addItem(itemDto, 1L));
        Assertions.assertEquals("Request ID 2 not found", exception.getMessage());
    }


    @Test
    @DisplayName("Find item exception")
    void testFindItemNotFound() {
        Mockito.when(itemService.getItemById(anyLong(), anyLong()))
                .thenThrow(new ItemNotFoundException("Item with ID 2 not found"));

        final ItemNotFoundException exception = Assertions.assertThrows(ItemNotFoundException.class,
                () -> itemService.getItemById(2L, 1L));
        Assertions.assertEquals("Item with ID 2 not found", exception.getMessage());
    }

    @Test
    @DisplayName("Update item")
    void testEditItem() {
        ItemDto oldItemDto = new ItemDto();
        oldItemDto.setId(1L);
        oldItemDto.setOwnerId(1L);
        oldItemDto.setAvailable(true);
        oldItemDto.setName("table");
        oldItemDto.setDescription("white table");

        Mockito.when(itemService.editItem(oldItemDto, 1L, 1L))
                .thenReturn(itemDto);
        Mockito.when(itemService.getItemById(1L, 1L))
                .thenReturn(new ItemDtoExtended(itemDto));

        Assertions.assertEquals(itemDto.getName(), itemService.getItemById(1L, 1L).getName());
        Assertions.assertEquals(itemDto.getDescription(), itemService.getItemById(1L, 1L).getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), itemService.getItemById(1L, 1L).getAvailable());
    }

    @Test
    @DisplayName("Get owner items wrong request parameters")
    void testGetAllOwnerItems() {
        Mockito.when(itemService.getAllOwnerItems(anyInt(), anyInt(), anyLong()))
                .thenThrow(new InvalidEntityException("Invalid request parameter"));

        final InvalidEntityException exception = Assertions.assertThrows(InvalidEntityException.class,
                () -> itemService.getAllOwnerItems(anyInt(), anyInt(), anyLong()));
        Assertions.assertEquals("Invalid request parameter", exception.getMessage());
    }

    @Test
    @DisplayName("Search items for booking")
    void testSearchItemsForBooking() {
        Mockito.when(itemService.searchItemsForBooking(1, 1, ""))
                .thenReturn(List.of());
        Assertions.assertEquals(List.of(), itemService.searchItemsForBooking(1, 1, ""));

    }

    @Test
    @DisplayName("Add comment to items user not found")
    void testAddCommentUserNotFound() {
        UserDto userDto1 = new UserDto();
        userDto1.setName("Scott");
        userDto1.setEmail("scott@email.com");
        CommentCreationDto creationDto = new CommentCreationDto();
        creationDto.setText("super item");
        CommentDto commentDto = new CommentDto(null, "super item", userDto1.getName(), LocalDateTime.now());
        Mockito.when(itemService.addComment(1L, 1L, creationDto))
                .thenReturn(commentDto);
        Assertions.assertEquals(commentDto, itemService.addComment(1L, 1L, creationDto));

    }

    @Test
    @DisplayName("Search items for item")
    void testSearchCommentsForItem() {
        Mockito.when(itemService.searchCommentsForItem(1, ""))
                .thenReturn(List.of());
        Assertions.assertEquals(List.of(), itemService.searchCommentsForItem(1,""));

    }
}
