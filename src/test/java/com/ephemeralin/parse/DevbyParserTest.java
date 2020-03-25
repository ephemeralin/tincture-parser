package com.ephemeralin.parse;

import com.ephemeralin.data.RssEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ephemeralin.parse.Constants.MAX_FEED_SIZE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DevbyParserTest {

    @Test
    @DisplayName("Dev.by parser test")
    void parse() {
        DevbyParser parser = new DevbyParser();
        List<RssEntry> list = parser.parse("https://dev.by/rss");
        assertNotNull(list);
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }
}