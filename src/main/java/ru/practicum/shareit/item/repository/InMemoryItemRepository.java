package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundIdException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private Map<Integer, Item> itemMap = new HashMap<>();
    private Integer id = 0;

    @Override
    public Item createItem(Item item) {
        item.setId(getId());
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        Integer id = item.getId();
        if (id == null || !itemMap.containsKey(id)) {
            throw new NotFoundIdException("Item id: " + id + " not found");
        }
        Item existingItem = itemMap.get(id);
        existingItem.setName(item.getName());
        existingItem.setDescription(item.getDescription());
        if (item.getAvailable() != null) {
            existingItem.setAvailable(item.getAvailable());
        }

        return item;
    }

    @Override
    public Optional<Item> getItemById(Integer id) {
        return Optional.ofNullable(itemMap.get(id));
    }

    @Override
    public List<Item> getItems(Integer userId) {
        return itemMap.values().stream().filter(item -> item.getOwner().equals(userId)).toList();
    }

    @Override
    public List<Item> searchItem(String text) {
        return itemMap.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                        item.getName().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    private Integer getId() {
        return ++id;
    }
}
