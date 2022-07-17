package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, long userId);

    ItemDto editItem(ItemDto itemDto, long itemId, long userId);

    ItemDto getItemById(long id);

    Collection<ItemDto> getAllOwnerItems(long userId);

    Collection<ItemDto> searchItemsForBooking(String text);
}
