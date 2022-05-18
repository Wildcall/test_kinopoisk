package ru.malygin.parser.service.impl;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.malygin.parser.context.annotation.InjectObj;
import ru.malygin.parser.context.annotation.InjectProp;
import ru.malygin.parser.service.CookieService;
import ru.malygin.parser.service.FetcherService;

import java.io.IOException;

public class FetcherServiceImpl implements FetcherService {

    @InjectObj
    private CookieService cookieService;

    @InjectProp
    private String userAgent;
    @InjectProp
    private String referrer;
    @InjectProp
    private String timeout;

    @Override
    public Document fetch(String path) {
        try {
            Connection.Response response = Jsoup
                    .connect(path)
                    .maxBodySize(0)
                    .userAgent(userAgent)
                    .referrer(referrer)
                    .timeout(Integer.parseInt(timeout))
                    .cookies(cookieService.getCookies())
                    .execute();
            return response.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
