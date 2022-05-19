package ru.malygin.parser.context;

import ru.malygin.parser.context.config.FilePropertyResolver;
import ru.malygin.parser.context.config.JavaConfiguration;
import ru.malygin.parser.context.config.PropertyResolver;

public class Application {
    public static ApplicationContext run(Class<?> clazz) {
        String packageToScan = clazz.getPackageName();
        JavaConfiguration configuration = new JavaConfiguration(packageToScan);
        PropertyResolver propertyResolver = new FilePropertyResolver();
        ApplicationContext context = new ApplicationContext(configuration, propertyResolver);
        ObjectFactory objectFactory = new ObjectFactory(context);

        context.setFactory(objectFactory);
        return context;
    }
}
