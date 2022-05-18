package ru.malygin.parser.context;

import lombok.Getter;
import lombok.Setter;
import ru.malygin.parser.context.annotation.Singleton;
import ru.malygin.parser.context.ifc.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {

    private final Map<Class, Object> cache = new ConcurrentHashMap<>();
    @Setter
    private ObjectFactory factory;
    @Getter
    private Configuration configuration;

    public ApplicationContext(Configuration configuration) {
        this.configuration = configuration;
    }

    public <T> T getObject(Class<T> type) {
        if (cache.containsKey(type)) {
            return (T) cache.get(type);
        }

        Class<? extends T> implClass = type;

        if (type.isInterface()) {
            implClass = configuration.getImplClass(type);
        }
        T t = null;
        try {
            t = factory.createObject(implClass);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        if (implClass.isAnnotationPresent(Singleton.class)) {
            cache.put(type, t);
        }

        return t;
    }
}
