package ru.yandex.practicum.filmorate.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String s) {
        super(s);
    }
}