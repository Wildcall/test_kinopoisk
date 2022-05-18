package ru.malygin.parser.service.impl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.malygin.parser.context.annotation.InjectObj;
import ru.malygin.parser.context.annotation.InjectProp;
import ru.malygin.parser.entity.Film;
import ru.malygin.parser.service.CookieService;
import ru.malygin.parser.service.FetcherService;
import ru.malygin.parser.service.SiteParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SiteParserImpl implements SiteParser {

    @InjectObj
    private CookieService cookieService;
    @InjectObj
    private FetcherService fetcherService;

    @InjectProp
    private String path;
    @InjectProp
    private String host;

    @InjectProp
    private String blockClass;
    @InjectProp
    private String mainInfo;
    @InjectProp
    private String secondaryInfo;
    @InjectProp
    private String secondaryInfo3pcsPattern;
    @InjectProp
    private String secondaryInfo2pcsPattern;
    @InjectProp
    private String ratingInfo;
    @InjectProp
    private String ratingInfo3pcsPattern;
    @InjectProp
    private String voteCount;
    @InjectProp
    private String voteCountPattern;

    @Override
    public List<Film> process() {
        Document document = fetcherService.fetch(path + 1);
        return new ArrayList<>(parseClassBlock(document));
    }

    private List<Film> parseClassBlock(Document document) {
        if (document == null) throw new RuntimeException("Document is null");

        List<Film> films = new ArrayList<>();

        getFilmBlocks(document).forEach(element -> {
            try {
                String[] nameYear = getNameYear(element);
                String[] ratingPos = getRatingPos(element);
                Integer voteCount = getVoteCount(element);

                Film film = new Film(Integer.parseInt(ratingPos[1]),
                                     Double.parseDouble(ratingPos[0]),
                                     nameYear[0],
                                     Integer.parseInt(nameYear[1]),
                                     voteCount);
                System.out.println(film);
                films.add(film);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        });
        return films;
    }

    private Integer getVoteCount(Element element) {
        Elements elements = element.getElementsByAttribute("href");
        Document document = fetcherService.fetch(this.host + elements.attr("href"));

        if (document == null)
            return null;

        Element rating = document
                .getElementsByClass(this.voteCount)
                .first();
        if (rating == null)
            throw new RuntimeException(
                    "Can't get vote count from " + this.host + elements.attr("href"));

        String voteCount = rating.text();
        Pattern pattern = Pattern.compile(this.voteCountPattern);
        Matcher matcher = pattern.matcher(voteCount);

        if (matcher.find())
            return Integer.parseInt(matcher
                                            .group(1)
                                            .replaceAll(" ", ""));
        return null;
    }

    private Elements getFilmBlocks(Document document) {
        if (document == null) throw new RuntimeException("Document is null");
        return document.getElementsByClass(this.blockClass);
    }

    private String[] getNameYear(Element element) {
        String mainInfo = element
                .getElementsByClass(this.mainInfo)
                .text();

        String secondaryInfo = element
                .getElementsByClass(this.secondaryInfo)
                .text();

        Pattern pattern = Pattern.compile(this.secondaryInfo3pcsPattern);
        Matcher matcher = pattern.matcher(secondaryInfo);

        if (matcher.find()) return new String[]{matcher.group(1).trim(), matcher.group(2).trim()};


        pattern = Pattern.compile(this.secondaryInfo2pcsPattern);
        matcher = pattern.matcher(secondaryInfo);

        if (matcher.find()) return new String[]{mainInfo.trim(), matcher.group(1).trim()};


        throw new RuntimeException("Parsing error with: " + secondaryInfo);
    }

    private String[] getRatingPos(Element element) {
        String ratingInfo = element
                .getElementsByClass(this.ratingInfo)
                .text();

        Pattern pattern = Pattern.compile(this.ratingInfo3pcsPattern);
        Matcher matcher = pattern.matcher(ratingInfo);

        if (matcher.find()) return new String[]{matcher.group(1).trim(), matcher.group(3).trim()};

        throw new RuntimeException("Parsing error with: " + ratingInfo);
    }
}
