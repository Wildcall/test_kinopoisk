package ru.malygin.parser.service;

import java.util.Map;

public interface CookieService {

    /**
     * Returns map containing cookies
     *
     * @return the {@link Map}<{@link String}, {@link String}> with cookie
     */
    Map<String, String> getCookies();
}
