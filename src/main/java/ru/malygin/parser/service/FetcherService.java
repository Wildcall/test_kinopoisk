package ru.malygin.parser.service;

import org.jsoup.nodes.Document;

public interface FetcherService {

    /**
     * Fetch the content of the site specified in the parameters.
     * The parameters are set in the file fetcher.properties
     *
     * @param path the Url from which the {@link Document} will be received
     * @return the {@link Document} containing film data
     */
    Document fetch(String path);
}
