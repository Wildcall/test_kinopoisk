package ru.malygin.parser.context.ifc;

import org.reflections.Reflections;

public interface Configuration {

    Reflections getScanner();

    <T> Class<? extends T> getImplClass(Class<T> ifc);
}
