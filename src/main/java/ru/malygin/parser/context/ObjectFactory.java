package ru.malygin.parser.context;

import ru.malygin.parser.context.ifc.ObjectConfigurator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ObjectFactory {

    private final ApplicationContext context;
    private final List<ObjectConfigurator> configurators;

    public ObjectFactory(ApplicationContext context) {
        this.context = context;
        this.configurators = new ArrayList<>();
        try {
            for (Class<? extends ObjectConfigurator> aClass : context
                    .getConfiguration()
                    .getScanner()
                    .getSubTypesOf(ObjectConfigurator.class)) {
                configurators.add(aClass
                                          .getDeclaredConstructor()
                                          .newInstance());
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    public <T> T createObject(Class<T> implClass) throws ReflectiveOperationException {
        T t = create(implClass);
        configure(t);
        return t;
    }

    private <T> T create(Class<T> implClass) throws ReflectiveOperationException {
        return implClass
                .getDeclaredConstructor()
                .newInstance();
    }

    private <T> void configure(T t) {
        configurators.forEach(objectConfigurator -> objectConfigurator.configure(t, context));
    }
}
