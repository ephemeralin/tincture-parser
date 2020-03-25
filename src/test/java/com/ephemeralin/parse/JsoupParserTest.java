package com.ephemeralin.parse;

import com.ephemeralin.data.RssEntry;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ephemeralin.parse.Constants.MAX_FEED_SIZE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
class JsoupParserTest {

    @Test
    @DisplayName("Vc parser test")
    void parse() {
        JsoupParser parser = new JsoupParser();
        List<RssEntry> list = parser.parse("https://vc.ru/rss/all/");
        assertNotNull(list);
        assertTrue(list.size() != 0, "List must not be empty");
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("PopMech parser test")
    void parsePopMech() {
        JsoupParser parser = new JsoupParser();
        List<RssEntry> list = parser.parse("https://www.popmech.ru/out/public-all.xml");
        assertNotNull(list);
        assertTrue(list.size() != 0, "List must not be empty");
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("Ixbt")
    void parseIxbt() {
        JsoupParser parser = new JsoupParser();
        List<RssEntry> list = parser.parse("https://www.ixbt.com/export/news.rss");
        assertNotNull(list);
        assertTrue(list.size() != 0, "List must not be empty");
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("Rbc")
    void parseRbc() {
        JsoupParser parser = new JsoupParser();
        List<RssEntry> list = parser.parse("http://static.feed.rbc.ru/rbc/logical/footer/news.rss");
        assertNotNull(list);
        assertTrue(list.size() != 0, "List must not be empty");
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("Developer.com")
    void parseDevcom() {
        JsoupParser parser = new JsoupParser();
        List<RssEntry> list = parser.parse("https://www.developer.com/developer/dev-25.xml");
        assertNotNull(list);
        assertTrue(list.size() != 0, "List must not be empty");
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @DisplayName("Techcrunch")
    void parseTechcrunch() {
        JsoupParser parser = new JsoupParser();
        List<RssEntry> list = parser.parse("https://techcrunch.com/feed/");
        assertNotNull(list);
        assertTrue(list.size() != 0, "List must not be empty");
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

    @Test
    @Disabled
    @DisplayName("Any feed source")
    void parseAny() {
        JsoupParser parser = new JsoupParser();
//        List<RssEntry> list = parser.parse("https://www.reddit.com/r/programming.rss");
        List<RssEntry> list = parser.parse("https://www.reddit.com/r/GameDeals/new/.rss?sort=new");
        assertNotNull(list);
        assertTrue(list.size() != 0, "List must not be empty");
        assertTrue(list.size() > MAX_FEED_SIZE - 1);
        System.out.println(list.get(0).toString());
    }

}