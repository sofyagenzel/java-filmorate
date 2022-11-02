package ru.yandex.practicum.filmorate.exception;

import org.springframework.dao.EmptyResultDataAccessException;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String s) {
        super(s);
    }
}