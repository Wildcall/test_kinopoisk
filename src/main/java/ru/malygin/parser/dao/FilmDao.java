package ru.malygin.parser.dao;

import ru.malygin.parser.entity.Film;

import java.util.List;

public interface FilmDao {
    void saveAll(List<Film> films);
}
