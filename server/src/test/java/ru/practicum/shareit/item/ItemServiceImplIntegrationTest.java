package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @PersistenceContext
    private EntityManager em;

    private User owner;
    private User booker;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@test.com");
        userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@test.com");
        userRepository.save(booker);

        em.flush();
    }

    @Test
    void createItem_shouldSaveItemWithOwner() {
        CreateItemDto dto = new CreateItemDto();
        dto.setName("Drill");
        dto.setDescription("Power drill");
        dto.setAvailable(true);

        ItemDto result = itemService.createItem(dto, owner.getId());

        assertNotNull(result.getId());
        assertEquals("Drill", result.getName());
        assertEquals("Power drill", result.getDescription());
    }

    @Test
    void getItemById_shouldReturnItemWithComments() {
        CreateItemDto createDto = new CreateItemDto();
        createDto.setName("Item 1");
        createDto.setDescription("Description 1");
        createDto.setAvailable(true);

        ItemDto itemDto = itemService.createItem(createDto, owner.getId());

        ResponseItemDto result = itemService.getItemById(itemDto.getId(), owner.getId());

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
    }

    @Test
    void getUserItems_shouldReturnOwnerItems() {
        CreateItemDto createDto = new CreateItemDto();
        createDto.setName("Item 1");
        createDto.setDescription("Description 1");
        createDto.setAvailable(true);

        itemService.createItem(createDto, owner.getId());

        List<ResponseItemDto> items = itemService.getUserItems(owner.getId());

        assertFalse(items.isEmpty());
        assertEquals(1, items.size());
        assertEquals("Item 1", items.get(0).getName());
    }

    @Test
    void searchItem_shouldReturnMatchingItems() {
        CreateItemDto createDto = new CreateItemDto();
        createDto.setName("Drill");
        createDto.setDescription("Power tool");
        createDto.setAvailable(true);
        itemService.createItem(createDto, owner.getId());

        List<ItemDto> result = itemService.searchItem(owner.getId(), "drill");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Drill", result.get(0).getName());
    }
}