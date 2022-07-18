package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, long userId);

    ItemDto editItem(ItemDto itemDto, long itemId, long userId);

    ItemDto getItemById(long id);

    List<ItemDto> getAllOwnerItems(long userId);

    List<ItemDto> searchItemsForBooking(String text);
}
