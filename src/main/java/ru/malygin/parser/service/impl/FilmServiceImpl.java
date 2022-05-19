package ru.malygin.parser.service.impl;

import ru.malygin.parser.context.annotation.InjectObj;
import ru.malygin.parser.context.annotation.InjectProp;
import ru.malygin.parser.context.annotation.Singleton;
import ru.malygin.parser.dao.FilmDao;
import ru.malygin.parser.entity.Film;
import ru.malygin.parser.service.FilmService;
import ru.malygin.parser.service.LogService;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Singleton
public class FilmServiceImpl implements FilmService {

    @InjectObj
    private FilmDao filmDao;
    @InjectObj
    private LogService logService;

    @InjectProp
    private Boolean saveInDb;
    @InjectProp
    private Boolean logFilms;

    @Override
    public void saveAll(List<Film> films) {
        long data = System.currentTimeMillis();
        AtomicBoolean hasError = new AtomicBoolean(false);
        List<Film> filmsToSave = films
                .stream()
                .peek(film -> {
                    film.setInsertDate(new Date(data));
                    boolean error = film.hasError();
                    if (error) {
                        logService.error("Film has error: {}", film);
                        hasError.set(true);
                    }
                    if (logFilms && !saveInDb && !error)
                        logService.info("Film: {}", film);
                })
                .toList();

        if (saveInDb && !hasError.get())
            filmDao.saveAll(filmsToSave);

        if (logFilms && !hasError.get())
            filmsToSave.forEach(
                    film ->
                            logService.info("Film: {}", film));
    }
}
