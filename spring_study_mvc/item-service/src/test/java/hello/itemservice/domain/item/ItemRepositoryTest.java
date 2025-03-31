package hello.itemservice.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    public void afterEach() {
        itemRepository.clearStore();
    }

    @Test
    void save() {
        // given
        Item itemA = new Item("itemA", 1000, 5);

        // when
        Item savedItem = itemRepository.save(itemA);

        // then
        Item findItem = itemRepository.findById(savedItem.getId());
        assertThat(findItem).isSameAs(savedItem);
    }

    @Test
    void findAll() {
        // givne
        Item itemA = new Item("itemA", 1000, 5);
        Item itemB = new Item("itemB", 1000, 5);

        itemRepository.save(itemA);
        itemRepository.save(itemB);

        // when
        List<Item> itemList = itemRepository.findAll();

        // then
        assertThat(itemList.size()).isEqualTo(2);
        assertThat(itemList).contains(itemA, itemB);
    }

    @Test
    void update() {
        // given
        Item itemA = new Item("itemA", 10000, 10);

        Item savedItem = itemRepository.save(itemA);
        Long itemId = savedItem.getId();

        // when
        Item itemB = new Item("itemB", 20000, 25);
        itemRepository.update(itemId, itemB);

        // then
        Item findItem = itemRepository.findById(itemId);
        assertThat(findItem.getPrice()).isEqualTo(20000);
        assertThat(findItem.getItemName()).isEqualTo("itemB");
        assertThat(findItem.getQuantity()).isEqualTo(25);
    }
}