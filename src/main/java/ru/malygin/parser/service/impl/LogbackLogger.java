package ru.malygin.parser.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.malygin.parser.service.LogService;

public class LogbackLogger implements LogService {

    private final Logger errorLogger;
    private final Logger infoLogger;
    private final Logger warnLogger;

    public LogbackLogger() {
        this.errorLogger = LoggerFactory.getLogger("error");
        this.infoLogger = LoggerFactory.getLogger("info");
        this.warnLogger = LoggerFactory.getLogger("warn");
    }

    @Override
    public void error(String var1,
                      Object... var2) {
        errorLogger.error(var1, var2);
    }

    @Override
    public void info(String var1,
                     Object... var2) {
        infoLogger.info(var1, var2);
    }

    @Override
    public void warn(String var1,
                     Object... var2) {
        warnLogger.warn(var1, var2);
    }
}
