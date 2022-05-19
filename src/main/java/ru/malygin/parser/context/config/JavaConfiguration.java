package ru.malygin.parser.context.config;

import lombok.Getter;
import org.reflections.Reflections;
import ru.malygin.parser.context.util.Assert;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class JavaConfiguration implements Configuration {

    @Getter
    private final Reflections scanner;
    private final Map<Class, Class> ifc2ImplClass = new ConcurrentHashMap<>();

    public JavaConfiguration(String pck) {
        this.scanner = new Reflections(pck);
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<T> ifc) {
        return ifc2ImplClass.computeIfAbsent(ifc, aClass -> {
            Set<Class<? extends T>> classes = scanner.getSubTypesOf(ifc);
            if (classes.size() != 1) {
                throw new RuntimeException(ifc + " has 0 or more than one impl");
            }
            return classes
                    .iterator()
                    .next();
        });
    }

    @Override
    public <T> void addImplClass2Ifc(Class<T> ifc,
                                     Class<? extends T> impl) {
        Assert.notNull(ifc, "ifc must not be null");
        Assert.notNull(impl, "impl must not be null");
        if (impl.isInterface()) {
            throw new IllegalArgumentException("impl must not be ifc");
        }
        ifc2ImplClass.put(ifc, impl);
    }
}
