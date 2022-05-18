package ru.malygin.parser.service;

import ru.malygin.parser.entity.Film;

import java.util.List;

public interface SiteParser {
    List<Film> process();
}
