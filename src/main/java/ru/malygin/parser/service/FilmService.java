package ru.malygin.parser.service;

import ru.malygin.parser.entity.Film;

import java.util.List;

public interface FilmService {
    /**
     * Adds the current date to each film,
     * checks params,
     * and sends the list to the database to saving
     *
     * @param films the list of {@link Film}
     */
    void saveAll(List<Film> films);
}
