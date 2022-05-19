package ru.malygin.parser.service.impl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.malygin.parser.context.annotation.InjectObj;
import ru.malygin.parser.context.annotation.InjectProp;
import ru.malygin.parser.context.annotation.PropertySource;
import ru.malygin.parser.context.annotation.Singleton;
import ru.malygin.parser.context.util.Assert;
import ru.malygin.parser.entity.Film;
import ru.malygin.parser.service.LogService;
import ru.malygin.parser.service.ParseService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Singleton
@PropertySource(source = "parser.properties")
public class ParseServiceImpl implements ParseService {

    @InjectObj
    private LogService logService;

    @InjectProp
    private String classWithAllFilmInfo;
    @InjectProp
    private String classMainTitle;
    @InjectProp
    private String classSubTittle;
    @InjectProp
    private String regexSubTitle3pcs;
    @InjectProp
    private String regexSubTitle2pcs;
    @InjectProp
    private String classRating;
    @InjectProp
    private String classVoteCount;
    @InjectProp
    private String classPos;
    @InjectProp
    private String regexPos1pcs;

    @Override
    public List<Film> parse(Document document) {
        Assert.notNull(document, "Document must be not null");

        return getStreamOfBlocksWithFilmInfo(document)
                .map(element -> {
                    Integer pos = getPos(element);
                    Double rating = getRating(element);
                    String[] tmp = getNameYear(element);
                    String title = tmp[0];
                    Integer prodYear = Integer.valueOf(tmp[1]);
                    Integer voteCount = getVoteCount(element);
                    Film film = new Film(pos, rating, title, prodYear, voteCount);
                    return film;
                })
                .toList();
    }

    private Stream<Element> getStreamOfBlocksWithFilmInfo(Document document) {
        return document
                .getElementsByClass(this.classWithAllFilmInfo)
                .stream();
    }

    private String[] getNameYear(Element element) {
        String mainTitle = element
                .getElementsByClass(this.classMainTitle)
                .text();

        String subTitle = element
                .getElementsByClass(this.classSubTittle)
                .text();

        Pattern pattern = Pattern.compile(this.regexSubTitle3pcs);
        Matcher matcher = pattern.matcher(subTitle);

        if (matcher.find()) return new String[]{matcher.group(1).trim(), matcher.group(2).trim()};

        pattern = Pattern.compile(this.regexSubTitle2pcs);
        matcher = pattern.matcher(subTitle);

        if (matcher.find()) return new String[]{mainTitle.trim(), matcher.group(1).trim()};

        return new String[]{"-1", "-1"};
    }

    private Integer getPos(Element element) {
        String text = element
                .getElementsByClass(classPos)
                .text()
                .trim();

        Pattern pattern = Pattern.compile(this.regexPos1pcs);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find())
            return -1;

        text = matcher.group(1);

        try {
            return Integer.valueOf(text);
        } catch (IllegalArgumentException e) {
            logService.warn("Can't parse value: {} | Cause: {}", text, e.getMessage());
        }

        return -1;
    }

    private Double getRating(Element element) {
        String text = element
                .getElementsByClass(classRating)
                .text()
                .trim();
        try {
            return Double.valueOf(text);
        } catch (IllegalArgumentException e) {
            logService.warn("Can't parse value: {} | Cause: {}", text, e.getMessage());
        }

        return -1.0;
    }

    private Integer getVoteCount(Element element) {
        String text = element
                .getElementsByClass(classVoteCount)
                .text()
                .replaceAll(" ", "");
        try {
            return Integer.valueOf(text);
        } catch (IllegalArgumentException e) {
            logService.warn("Can't parse value: {} | Cause: {}", text, e.getMessage());
        }

        return -1;
    }
}
