package ru.malygin.parser.context.config;

public interface PropertyResolver {
    ResolvedPropertySource resolve(String sourcePath);
}

