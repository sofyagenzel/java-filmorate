package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.validation.After;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@SuperBuilder
public class Film {
    int id;
    @Size(min = 1, max = 200) String description;
    @NotEmpty(message = "название не может быть пустым") String name;
    @Positive int duration;
    @After("1895-12-28")
    @NotNull LocalDate releaseDate;
    private Set<Integer> listLikes = new HashSet<>();
    @NotNull
    private MPA mpa;

    private List<Genre> genres = new ArrayList<>();

    public Film() {
    }

    public Film(int id, String description, String name, int duration, LocalDate releaseDate, MPA mpa, List<Genre> genres) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.mpa = mpa;
        this.genres = genres;
    }

    public void addLike(int userId) {
        listLikes.add(userId);
    }

    public void deleteLike(int userId) {
        listLikes.remove(userId);
    }
}

