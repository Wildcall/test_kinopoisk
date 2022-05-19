package ru.malygin.parser.service.impl;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.malygin.parser.context.annotation.InjectObj;
import ru.malygin.parser.context.annotation.InjectProp;
import ru.malygin.parser.context.annotation.PropertySource;
import ru.malygin.parser.context.annotation.Singleton;
import ru.malygin.parser.service.CookieService;
import ru.malygin.parser.service.FetcherService;
import ru.malygin.parser.service.LogService;

import java.io.IOException;

@Singleton
@PropertySource(source = "fetcher.properties")
public class FetcherServiceImpl implements FetcherService {

    @InjectObj
    private CookieService cookieService;
    @InjectObj
    private LogService logService;

    @InjectProp
    private String userAgent;
    @InjectProp
    private String referrer;
    @InjectProp
    private Integer timeout;

    @Override
    public Document fetch(String path) {
        try {
            Connection.Response response = Jsoup
                    .connect(path)
                    .maxBodySize(0)
                    .userAgent(userAgent)
                    .referrer(referrer)
                    .timeout(timeout)
                    .cookies(cookieService.getCookies())
                    .execute();
            return response.parse();
        } catch (IOException e) {
            logService.error("Connection error: " + e.getMessage());
        }
        return null;
    }
}
