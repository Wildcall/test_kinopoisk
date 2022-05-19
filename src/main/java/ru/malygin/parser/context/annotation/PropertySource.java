package ru.malygin.parser.context.annotation;

import ru.malygin.parser.context.config.FilePropertyResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.TYPE)
@Retention(RUNTIME)
public @interface PropertySource {
    public String source() default FilePropertyResolver.DEFAULT_SOURCE;
}
