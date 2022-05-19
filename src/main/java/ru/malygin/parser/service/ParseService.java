package ru.malygin.parser.service;

import org.jsoup.nodes.Document;
import ru.malygin.parser.entity.Film;

import java.util.List;

public interface ParseService {

    /**
     * Returns the list of {@link Film} with filled fields from the Document.
     * The data will be filled according to the settings in the file parser.properties,
     * if the value is not received, or can not be resolved will be filed with -1
     *
     * @param document еhe {@link Document} from which the list of films is to be obtained
     * @return the list of {@link Film}
     */
    List<Film> parse(Document document);
}
