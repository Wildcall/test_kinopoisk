package ru.malygin.parser.service.impl;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.malygin.parser.context.annotation.InjectObj;
import ru.malygin.parser.context.annotation.InjectProp;
import ru.malygin.parser.context.annotation.PostConstruct;
import ru.malygin.parser.context.annotation.Singleton;
import ru.malygin.parser.service.CookieService;
import ru.malygin.parser.service.LogService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class CookieServiceImpl implements CookieService {

    @InjectObj
    private LogService logger;
    @InjectProp
    private String cookiePath;
    @Getter
    private Map<String, String> cookies;

    @PostConstruct
    public void init() {
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
            logger.warn("Cookie not found. Path: {}", cookiePath);
        }
    }
}
