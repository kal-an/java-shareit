package ru.practicum.shareit.requests;

public class RequestNotFoundException extends RuntimeException {

    public RequestNotFoundException(String message) {
        super(message);
    }
}
