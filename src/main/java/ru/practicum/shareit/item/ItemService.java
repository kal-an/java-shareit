package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, long userId);

    ItemDto editItem(ItemDto itemDto, long itemId, long userId);

    ItemDtoExtended getItemById(long id, long userId);

    List<ItemDtoExtended> getAllOwnerItems(int fromPage, int size, long userId);

    List<ItemDto> searchItemsForBooking(int fromPage, int size, String text);

    CommentDto addComment(long userId, long itemId, CommentCreationDto text);

    List<CommentDto> searchCommentsForItem(long itemId, String text);
}
