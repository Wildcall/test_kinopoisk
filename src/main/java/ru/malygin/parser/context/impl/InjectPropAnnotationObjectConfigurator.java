package ru.malygin.parser.context.impl;

import ru.malygin.parser.context.ApplicationContext;
import ru.malygin.parser.context.annotation.InjectProp;
import ru.malygin.parser.context.ifc.ObjectConfigurator;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class InjectPropAnnotationObjectConfigurator implements ObjectConfigurator {

    private final String configFile = "app.properties";
    private final Map<String, String> propertiesMap = new ConcurrentHashMap<>();

    public InjectPropAnnotationObjectConfigurator() {
        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(configFile)) {

            if (inputStream == null) {
                throw new RuntimeException("Configuration file error " + configFile);
            }
            Properties properties = new Properties();
            properties.load(inputStream);
            properties.forEach((key, val) -> propertiesMap.put((String) key, (String) val));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void configure(Object t,
                          ApplicationContext context) {
        try {
            Class<?> implClass = t.getClass();
            for (Field field : implClass.getDeclaredFields()) {
                InjectProp annotation = field.getAnnotation(InjectProp.class);
                if (annotation != null) {
                    String value = propertiesMap.get(field.getName());
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    field.set(t, value);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
