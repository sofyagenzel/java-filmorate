package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MPA {
    private int id;
    private String name;

    public MPA(int id, String name) {
        this.id = id;
        this.name = name;
    }
}