package ru.malygin.parser.service.impl;

import org.jsoup.nodes.Document;
import ru.malygin.parser.context.annotation.InjectObj;
import ru.malygin.parser.context.annotation.InjectProp;
import ru.malygin.parser.context.annotation.Singleton;
import ru.malygin.parser.entity.Film;
import ru.malygin.parser.service.*;

import java.util.List;

@Singleton
public class KinopoiskTopImpl implements KinopoiskTop {

    @InjectObj
    private CookieService cookieService;
    @InjectObj
    private FetcherService fetcherService;
    @InjectObj
    private ParseService parseService;
    @InjectObj
    private FilmService filmService;
    @InjectObj
    private LogService logService;

    @InjectProp
    private String path;
    @InjectProp
    private Integer startPage;
    @InjectProp
    private Integer endPage;

    @Override
    public void process() {
        logService.info("Start parse Kinopoisk Top 250");
        if (!valid()) {
            logService.error("Error with startPage and endPage properties; {} {}", startPage, endPage);
            throw new IllegalArgumentException("Error with startPage and endPage properties;");
        }
        for (int i = startPage; i <= endPage; i++) {
            Document document = fetcherService.fetch(path + i);
            List<Film> films = parseService.parse(document);
            filmService.saveAll(films);
        }
        logService.info("Complete parse");
    }

    private boolean valid() {
        if (startPage == null || endPage == null) return false;
        if (startPage <= 0 || endPage <= 0) return false;
        return startPage <= endPage;
    }
}
