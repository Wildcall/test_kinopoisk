package ru.malygin.parser.context.impl;

import lombok.Getter;
import org.reflections.Reflections;
import ru.malygin.parser.context.ifc.Configuration;

import java.util.Map;
import java.util.Set;

public class JavaConfiguration implements Configuration {

    @Getter
    private final Reflections scanner;
    private final Map<Class, Class> ifc2ImplClass;

    public JavaConfiguration(String pck,
                             Map<Class, Class> ifc2ImplClass) {
        this.scanner = new Reflections(pck);
        this.ifc2ImplClass = ifc2ImplClass;
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<T> ifc) {
        return ifc2ImplClass.computeIfAbsent(ifc, aClass -> {
            Set<Class<? extends T>> classes = scanner.getSubTypesOf(ifc);
            if (classes.size() != 1) {
                throw new RuntimeException(ifc + " has 0 or more than one impl please update your config");
            }
            return classes
                    .iterator()
                    .next();
        });
    }
}
