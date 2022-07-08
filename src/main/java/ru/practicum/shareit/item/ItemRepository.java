package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Optional<Item> addItem(Item item);

    Optional<Item> editItem(Item item, long itemId);

    Optional<Item> getItemById(long id);

    Collection<Item> getAllItems();

    List<Item> searchItemsForBooking(String text);

}
