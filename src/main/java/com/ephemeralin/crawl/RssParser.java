package com.ephemeralin.crawl;

import com.ephemeralin.data.RssEntry;

import java.util.List;

public interface RssParser {
    List<RssEntry> parse(String url);
}
