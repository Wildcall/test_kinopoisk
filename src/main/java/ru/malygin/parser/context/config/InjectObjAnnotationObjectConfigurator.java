package ru.malygin.parser.context.config;

import ru.malygin.parser.context.ApplicationContext;
import ru.malygin.parser.context.annotation.InjectObj;
import ru.malygin.parser.context.util.Assert;

import java.lang.reflect.Field;

public class InjectObjAnnotationObjectConfigurator implements ObjectConfigurator {

    @Override
    public void configure(Object t,
                          ApplicationContext context) {
        try {
            for (Field field : t
                    .getClass()
                    .getDeclaredFields()) {
                if (field.isAnnotationPresent(InjectObj.class)) {
                    field.setAccessible(true);
                    Object object = context.getObject(field.getType());
                    Assert.notNull(object, "Creating object error: " + field.getType());
                    field.set(t, object);
                }
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
