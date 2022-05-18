package ru.malygin.parser;

import ru.malygin.parser.context.Application;
import ru.malygin.parser.context.ApplicationContext;
import ru.malygin.parser.entity.Film;
import ru.malygin.parser.service.SiteParser;

import java.util.List;

public class ParserApplication {

    public static void main(String[] args) {
        ApplicationContext context = Application.run(ParserApplication.class);
        SiteParser siteParser = context.getObject(SiteParser.class);
        List<Film> films = siteParser.process();
    }
}
