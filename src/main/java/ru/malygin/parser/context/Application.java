package ru.malygin.parser.context;

import ru.malygin.parser.context.impl.JavaConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Application {
    public static ApplicationContext run(Class clazz) {
        String packageToScan = clazz.getPackageName();
        JavaConfiguration configuration = new JavaConfiguration(packageToScan, new HashMap<>(Map.of()));
        ApplicationContext context = new ApplicationContext(configuration);
        ObjectFactory objectFactory = new ObjectFactory(context);

        context.setFactory(objectFactory);
        return context;
    }
}
