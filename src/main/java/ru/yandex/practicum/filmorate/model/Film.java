package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.After;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    int id;
    @Size(min = 1, max = 200) String description;
    @NotEmpty(message = "название не может быть пустым") String name;
    @Positive int duration;
    @After("1895-12-28")
    @NotNull LocalDate releaseDate;

    public Film() {
    }

    public Film(int id, String description, String name, int duration, LocalDate releaseDate) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.duration = duration;
        this.releaseDate = releaseDate;
    }

}





