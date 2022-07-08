package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    Item addItem(Item item, long userId);

    Item editItem(Item item, long itemId, long userId);

    Item getItemById(long id);

    Collection<Item> getAllOwnerItems(long userId);

    Collection<Item> searchItemsForBooking(String text);
}
