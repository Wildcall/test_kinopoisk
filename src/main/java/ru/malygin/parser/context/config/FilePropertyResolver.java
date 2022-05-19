package ru.malygin.parser.context.config;

import ru.malygin.parser.context.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class FilePropertyResolver implements PropertyResolver {

    public static final String DEFAULT_SOURCE = "app.properties";

    private final Map<String, ResolvedPropertySource> cache;

    public FilePropertyResolver() {
        this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public ResolvedPropertySource resolve(String sourcePath) {
        Assert.notNull(sourcePath, "Source path must not be null");
        ResolvedPropertySource propertySource = cache.get(sourcePath);
        if (propertySource == null) {
            propertySource = loadResolvedPropertySource(sourcePath);
            Assert.notNull(propertySource, "Error with load source: " + sourcePath);
            cache.put(sourcePath, propertySource);
        }
        return propertySource;
    }

    private ResolvedPropertySource loadResolvedPropertySource(String source) {
        Map<String, Object> map = new HashMap<>();
        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(source)) {
            if (inputStream == null) {
                throw new RuntimeException(source + " not found");
            }
            Properties properties = new Properties();
            properties.load(inputStream);
            properties.forEach((key, val) -> map.put((String) key, val));
            return new ResolvedPropertySource(source, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
