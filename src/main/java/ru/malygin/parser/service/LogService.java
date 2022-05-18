package ru.malygin.parser.service;

public interface LogService {
    void error(String var1, Object... var2);
    void info(String var1, Object... var2);
    void warn(String var1, Object... var2);
}
