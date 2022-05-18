package ru.malygin.parser.service;

import org.jsoup.nodes.Document;

public interface FetcherService {
    Document fetch(String path);
}
