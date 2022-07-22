package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, long userId);

    ItemDto editItem(ItemDto itemDto, long itemId, long userId);

    ItemDtoExtended getItemById(long id, long userId);

    List<ItemDtoExtended> getAllOwnerItems(long userId);

    List<ItemDto> searchItemsForBooking(String text);

}
