package ru.practicum.item.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.item.comment.dto.CreateCommentDto;
import ru.practicum.item.dto.CreateItemDto;
import ru.practicum.item.dto.UpdateItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(CreateItemDto createItemDto, Long userId) {
        return post("", userId, createItemDto);
    }

    public ResponseEntity<Object> updateItem(UpdateItemDto updateItemDto, Long itemId, Long userId) {
        return patch("/" + itemId, userId, updateItemDto);
    }

    public ResponseEntity<Object> getItemById(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getUserItems(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> searchItem(Long userId, String text) {
        Map<String, Object> parameter = Map.of("text", text);
        return get("/search", userId, parameter);
    }

    public ResponseEntity<Object> createComment(CreateCommentDto createCommentDto, Long itemId, Long userId) {
        return post("/" + itemId + "/comment", userId, createCommentDto);
    }
}
