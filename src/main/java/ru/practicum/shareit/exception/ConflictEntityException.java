package ru.practicum.shareit.exception;

public class ConflictEntityException extends RuntimeException{

    public ConflictEntityException(String message) {
        super(message);
    }
}
