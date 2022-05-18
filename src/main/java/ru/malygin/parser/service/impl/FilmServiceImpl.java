package ru.malygin.parser.service.impl;

import ru.malygin.parser.context.annotation.InjectObj;
import ru.malygin.parser.dao.FilmDao;
import ru.malygin.parser.entity.Film;
import ru.malygin.parser.service.FilmService;

public class FilmServiceImpl implements FilmService {

    @InjectObj
    private FilmDao filmDao;

    @Override
    public void save(Film film) {
        filmDao.save(film);
    }
}
