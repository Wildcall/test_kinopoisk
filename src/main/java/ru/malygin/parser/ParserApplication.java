package ru.malygin.parser;

import ru.malygin.parser.context.Application;
import ru.malygin.parser.context.ApplicationContext;
import ru.malygin.parser.service.KinopoiskTop;

public class ParserApplication {

    public static void main(String[] args) {
        ApplicationContext context = Application.run(ParserApplication.class);
        KinopoiskTop kinopoiskTop = context.getObject(KinopoiskTop.class);
        kinopoiskTop.process();
    }
}
