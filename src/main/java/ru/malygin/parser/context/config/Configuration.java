package ru.malygin.parser.context.config;

import org.reflections.Reflections;

public interface Configuration {

    Reflections getScanner();

    <T> Class<? extends T> getImplClass(Class<T> ifc);

    <T> void addImplClass2Ifc(Class<T> ifc, Class<? extends T> impl);
}
