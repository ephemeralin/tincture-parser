package com.ephemeralin.crawl;

import com.ephemeralin.data.RssEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsoupParserTest {

    @Test
    @DisplayName("Vc parser test")
    void parse() {
        JsoupParser parser = new JsoupParser();
        List<RssEntry> list = parser.parse("https://vc.ru/rss/all/");
        assertNotNull(list);
        assertTrue(list.size() != 0, "List must not be empty");
        assertTrue(list.size() > 9);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("PopMech parser test")
    void parsePopMech() {
        JsoupParser parser = new JsoupParser();
        List<RssEntry> list = parser.parse("https://www.popmech.ru/out/public-all.xml");
        assertNotNull(list);
        assertTrue(list.size() != 0, "List must not be empty");
        assertTrue(list.size() > 9);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("Ixbt")
    void parseIxbt() {
        JsoupParser parser = new JsoupParser();
        List<RssEntry> list = parser.parse("https://www.ixbt.com/export/news.rss");
        assertNotNull(list);
        assertTrue(list.size() != 0, "List must not be empty");
        assertTrue(list.size() > 9);
        System.out.println(list.get(0).toString());
    }

}