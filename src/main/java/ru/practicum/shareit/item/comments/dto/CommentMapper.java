package ru.practicum.shareit.item.comments.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.comments.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.List;

@UtilityClass
public class CommentMapper {
    public static Comment toComment(CreateCommentDto createCommentDto, Item item, User author, LocalDate created) {
        return new Comment(
                null,
                createCommentDto.getText(),
                item,
                author,
                created
        );
    }

    public static ResponseCommentDto toResponseCommentDto(Comment comment) {
        return new ResponseCommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static List<ResponseCommentDto> toResponseCommentDto(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toResponseCommentDto).toList();
    }
}
