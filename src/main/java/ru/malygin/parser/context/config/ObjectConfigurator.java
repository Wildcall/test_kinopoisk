package ru.malygin.parser.context.config;

import ru.malygin.parser.context.ApplicationContext;

public interface ObjectConfigurator {
    void configure(Object t,
                   ApplicationContext context);
}
