package com.ephemeralin.crawl;

import com.ephemeralin.data.RssEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RedditParserTest {

    @Test
    @DisplayName("Reddit prog")
    void parseRedditProg() {
        RedditParser parser = new RedditParser();
        List<RssEntry> list = parser.parse("https://www.reddit.com/r/programming/.rss?sort=hot");
        assertNotNull(list);
        assertTrue(list.size() > 9);
        System.out.println(list.get(3).toString());
    }

    @Test
    @DisplayName("Reddit tech")
    void parseRedditTech() {
        RedditParser parser = new RedditParser();
        List<RssEntry> list = parser.parse("https://www.reddit.com/r/technology/.rss?sort=hot");
        assertNotNull(list);
        assertTrue(list.size() > 9);
        System.out.println(list.get(3).toString());
    }
}