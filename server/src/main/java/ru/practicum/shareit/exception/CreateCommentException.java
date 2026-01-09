package ru.practicum.shareit.exception;

public class CreateCommentException extends RuntimeException {
    public CreateCommentException() {
    }

    public CreateCommentException(String message) {
        super(message);
    }
}
