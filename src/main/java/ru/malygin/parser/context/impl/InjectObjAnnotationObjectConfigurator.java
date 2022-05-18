package ru.malygin.parser.context.impl;

import ru.malygin.parser.context.ApplicationContext;
import ru.malygin.parser.context.annotation.InjectObj;
import ru.malygin.parser.context.ifc.ObjectConfigurator;

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
                    field.set(t, object);
                }
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
