package com.ephemeralin.crawl;

import com.ephemeralin.data.RssEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenericParserTest {

    @Test
    @DisplayName("Habr parser test")
    void parseHabr() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://habr.com/ru/rss/best/daily/");
        assertNotNull(list);
        assertTrue(list.size() > 9);
//        assertEquals(30, list.size(), "List size should equal 2");

    }

    @Test
    @DisplayName("Wylsa parser test")
    void parseWylsa() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://wylsa.com/category/news/feed/");
        assertNotNull(list);
        assertTrue(list.size() > 9);
        System.out.println(list.get(0).toString());
//        assertEquals(30, list.size(), "List size should equal 2");

    }
}