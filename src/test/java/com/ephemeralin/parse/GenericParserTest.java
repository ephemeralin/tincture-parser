package com.ephemeralin.parse;

import com.ephemeralin.data.RssEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ephemeralin.parse.Constants.MAX_FEED_SIZE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenericParserTest {

    @Test
    @DisplayName("Habr parser test")
    void parseHabr() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://habr.com/ru/rss/best/daily/");
        assertNotNull(list);
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
    }

    @Test
    @DisplayName("Wylsa parser test")
    void parseWylsa() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://wylsa.com/category/news/feed/");
        assertNotNull(list);
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("JUG.ru parser test")
    void parseJugRu() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://jug.ru/feed/");
        assertNotNull(list);
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("Meduza parser test")
    void parseMeduza() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://meduza.io/rss/all");
        assertNotNull(list);
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("Adme parser test")
    void parseAdme() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://www.adme.ru/rss/");
        assertNotNull(list);
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("Lenta parser test")
    void parseLenta() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://lenta.ru/rss");
        assertNotNull(list);
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("Kommersant parser test")
    void parseKommersant() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://www.kommersant.ru/RSS/news.xml");
        assertNotNull(list);
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("BBC Russia")
    void parseBbcRussia() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("http://feeds.bbci.co.uk/russian/rss.xml");
        assertNotNull(list);
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("Hacker News")
    void parseHn() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://news.ycombinator.com/rss");
        assertNotNull(list);
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("Techcrunch")
    void parseTechcrunch() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://techcrunch.com/feed/");
        assertNotNull(list);
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("JetBrains Blog")
    void parseJetBrains() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://blog.jetbrains.com/feed/");
        assertNotNull(list);
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("thedailywtf")
    void parseDailywtf() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("http://syndication.thedailywtf.com/TheDailyWtf");
        assertNotNull(list);
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("dev.to")
    void parseDevTo() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://dev.to/rss");
        assertNotNull(list);
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("any feed")
    void parseAnyFeed() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://github.blog/all.atom");
        assertNotNull(list);
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(2).toString());
    }

}