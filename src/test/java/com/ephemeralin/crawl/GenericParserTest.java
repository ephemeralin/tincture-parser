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
    }

    @Test
    @DisplayName("Wylsa parser test")
    void parseWylsa() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://wylsa.com/category/news/feed/");
        assertNotNull(list);
        assertTrue(list.size() > 9);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("JUG.ru parser test")
    void parseJugRu() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://jug.ru/feed/");
        assertNotNull(list);
        assertTrue(list.size() > 9);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("Meduza parser test")
    void parseMeduza() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://meduza.io/rss/all");
        assertNotNull(list);
        assertTrue(list.size() > 9);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("Adme parser test")
    void parseAdme() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://www.adme.ru/rss/");
        assertNotNull(list);
        assertTrue(list.size() > 9);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("Lenta parser test")
    void parseLenta() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://lenta.ru/rss");
        assertNotNull(list);
        assertTrue(list.size() > 9);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("Kommersant parser test")
    void parseKommersant() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://www.kommersant.ru/RSS/news.xml");
        assertNotNull(list);
        assertTrue(list.size() > 9);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("BBC Russia")
    void parseBbcRussia() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("http://feeds.bbci.co.uk/russian/rss.xml");
        assertNotNull(list);
        assertTrue(list.size() > 9);
        System.out.println(list.get(0).toString());
    }


}