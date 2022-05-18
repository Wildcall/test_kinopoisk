package ru.malygin.parser.service.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.malygin.parser.context.annotation.InjectObj;
import ru.malygin.parser.context.annotation.InjectProp;
import ru.malygin.parser.service.CookieService;
import ru.malygin.parser.service.LogService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public class CookieServiceImpl implements CookieService {

    private Map<String, String> cookies;
    @InjectObj
    private LogService logger;

    @InjectProp
    private String cookiePath;

    public Map<String, String> getCookies() {
        if (cookies == null) {
            cookies = new HashMap<>();
            try {
                String s = Files.readString(Path.of(cookiePath));
                JSONArray array = new JSONArray(s);
                array.forEach(o -> {
                    JSONObject jsonObject = (JSONObject) o;
                    String name = (String) jsonObject.get("name");
                    String value = (String) jsonObject.get("value");
                    cookies.put(name, value);
                });
                logger.info("Cookie successfully loaded | Count: {}", cookies.size());
            } catch (IOException e) {
                logger.error("Cause: {} | Path: {}", e.getMessage(), cookiePath);
                throw new RuntimeException("Cookies not found");
            }
        }
        return cookies;
    }
}
