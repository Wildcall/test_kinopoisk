package ru.malygin.parser.context;

import lombok.Getter;
import lombok.Setter;
import ru.malygin.parser.context.annotation.Singleton;
import ru.malygin.parser.context.config.Configuration;
import ru.malygin.parser.context.config.PropertyResolver;
import ru.malygin.parser.context.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {

    private final Map<Class<?>, Object> cache = new ConcurrentHashMap<>();
    @Getter
    private final Configuration configuration;
    @Getter
    private final PropertyResolver propertyResolver;
    @Setter
    private ObjectFactory factory;

    public ApplicationContext(Configuration configuration,
                              PropertyResolver propertyResolver) {
        this.configuration = configuration;
        this.propertyResolver = propertyResolver;
    }

    public <T> T getObject(Class<T> type) {
        Assert.notNull(type, "Required type must not be null");
        if (cache.containsKey(type)) {
            return (T) cache.get(type);
        }

        Class<? extends T> implClass = type;

        if (type.isInterface()) {
            implClass = configuration.getImplClass(type);
        }

        try {
            T t = factory.createObject(implClass);

            if (implClass.isAnnotationPresent(Singleton.class)) cache.put(type, t);

            return t;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        return null;
    }
}
