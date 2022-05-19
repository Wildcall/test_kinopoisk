package ru.malygin.parser.context.config;

import ru.malygin.parser.context.ApplicationContext;
import ru.malygin.parser.context.annotation.InjectProp;
import ru.malygin.parser.context.annotation.PropertySource;
import ru.malygin.parser.context.util.Assert;

import java.lang.reflect.Field;

public class InjectPropAnnotationObjectConfigurator implements ObjectConfigurator {

    @Override
    public void configure(Object t,
                          ApplicationContext context) {
        try {
            Class<?> implClass = t.getClass();
            ResolvedPropertySource propertySource = getResolvedPropertySource(context, implClass);

            for (Field field : implClass.getDeclaredFields()) {
                InjectProp annotation = field.getAnnotation(InjectProp.class);
                if (annotation != null) {
                    Object value = propertySource
                            .getPropertyMap()
                            .get(field.getName());
                    Assert.notNull(value,
                                   "Properties with name: " + field.getName() + " not found in " + propertySource.getSource());
                    field.setAccessible(true);
                    // todo автоматические парсить в нужный тип

                    field.set(t, cast(field.getType(), value));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private <T> T cast(Class<T> clazz,
                       Object o) {
        String val = (String) o;

        if (clazz.equals(String.class))
            return (T) val;

        if (clazz.equals(Boolean.class))
            return (T) Boolean.valueOf(val);

        if (val != null && val.trim().matches("^-?[0-9.]+$")) {
            if (clazz.equals(Integer.class))
                return (T) Integer.valueOf(val);

            if (clazz.equals(Double.class))
                return (T) Double.valueOf(val);

            if (clazz.equals(Float.class))
                return (T) Float.valueOf(val);

        }
        throw new RuntimeException(clazz + " not supported. @InjectProp support only String, Integer, Double, Float, Boolean");
    }

    private ResolvedPropertySource getResolvedPropertySource(ApplicationContext context,
                                                             Class<?> implClass) {
        PropertySource annotation = implClass.getAnnotation(PropertySource.class);
        String sourcePath = annotation == null ? FilePropertyResolver.DEFAULT_SOURCE : annotation.source();
        return context
                .getPropertyResolver()
                .resolve(sourcePath);
    }
}
