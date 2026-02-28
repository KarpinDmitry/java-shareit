package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i " +
            "WHERE i.available = TRUE " +
            "AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%')))")
    List<Item> search(@Param("text") String text);

    @Query("SELECT i FROM Item i WHERE i.ownerId = :userId")
    List<Item> getUserItems(@Param("userId") Long userId);

    @Query("SELECT i FROM Item i WHERE i.request.id IN :requestorsId")
    List<Item> getItemByRequests(@Param("requestorsId") List<Long> requestorsId);

    @Query("SELECT i FROM Item i JOIN i.request r WHERE r.id = :requestId")
    List<Item> getItemByRequest(@Param("requestId") Long requestId);
}
