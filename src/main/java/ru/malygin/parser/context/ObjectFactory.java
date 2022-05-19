package ru.malygin.parser.context;

import ru.malygin.parser.context.annotation.PostConstruct;
import ru.malygin.parser.context.config.ObjectConfigurator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ObjectFactory {

    private final ApplicationContext context;
    private final List<ObjectConfigurator> objConfigurators;

    public ObjectFactory(ApplicationContext context) {
        this.context = context;
        this.objConfigurators = new ArrayList<>();
        createObjectConfigurators(context);
    }

    public <T> T createObject(Class<T> implClass) throws ReflectiveOperationException {
        T t = create(implClass);
        configure(t);
        invokeInit(implClass, t);
        return t;
    }

    private <T> T create(Class<T> implClass) throws ReflectiveOperationException {
        return implClass
                .getDeclaredConstructor()
                .newInstance();
    }

    private <T> void invokeInit(Class<T> implClass,
                                T t) throws IllegalAccessException, InvocationTargetException {
        for (Method method : implClass.getMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.invoke(t);
            }
        }
    }

    private <T> void configure(T t) {
        objConfigurators.forEach(objectConfigurator -> objectConfigurator.configure(t, context));
    }

    private void createObjectConfigurators(ApplicationContext context) {
        try {
            for (Class<? extends ObjectConfigurator> aClass : context
                    .getConfiguration()
                    .getScanner()
                    .getSubTypesOf(ObjectConfigurator.class)) {
                objConfigurators.add(aClass
                                             .getDeclaredConstructor()
                                             .newInstance());
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
