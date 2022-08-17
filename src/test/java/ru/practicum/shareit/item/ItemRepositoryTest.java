package ru.practicum.shareit.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private ItemRepository repository;

    @Test
    @DisplayName("Searching items")
    void testSearchItemsForBooking() {
        User user = new User(null, "John", "john@email.com");
        Item item = new Item();
        item.setName("table");
        item.setDescription("white table");
        item.setAvailable(true);
        item.setOwner(user);
        Sort sortByEnd = Sort.by(Sort.Direction.DESC, "name");
        Pageable pageable = PageRequest.of(0, 10, sortByEnd);
        em.persist(user);
        assertNull(item.getId());
        em.persist(item);
        assertNotNull(item.getId());
        List<Item> items = repository.searchItemsForBooking("table", pageable);
        assertThat(items).isNotEmpty();
        assertThat(items.get(0).getName()).isEqualTo("table");
    }
}
