package com.ephemeralin.parse;

import com.ephemeralin.data.RssEntry;

import java.util.List;

public interface RssParser {
    List<RssEntry> parse(String url);
}
