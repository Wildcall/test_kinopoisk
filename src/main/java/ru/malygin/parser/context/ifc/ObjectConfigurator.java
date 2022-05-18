package ru.malygin.parser.context.ifc;

import ru.malygin.parser.context.ApplicationContext;

public interface ObjectConfigurator {
    void configure(Object t,
                   ApplicationContext context);
}
