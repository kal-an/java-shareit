package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, long userId);

    ItemDto editItem(ItemDto itemDto, long itemId, long userId);

    Item getItemById(long id);

    Collection<ItemDto> getAllOwnerItems(long userId);

    Collection<Item> searchItemsForBooking(String text);
}
