package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            "or upper(i.description) like upper(concat('%', ?1, '%'))" +
            "and i.available is true")
    List<Item> searchItemsForBooking(String text, Pageable pageable);

    List<Item> findItemsByOwnerId(long ownerId, Pageable pageable);

    List<Item> findByRequestId(long requestId);
}
