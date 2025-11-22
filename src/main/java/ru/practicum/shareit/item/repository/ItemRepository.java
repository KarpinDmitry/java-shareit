package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item createItem(Item item);

    Item updateItem(Item item);

    Optional<Item> getItemById(Integer id);

    List<Item> getItems(Integer userId);

    List<Item> searchItem(String text);
}
