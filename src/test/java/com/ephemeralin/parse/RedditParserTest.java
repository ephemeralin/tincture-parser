package com.ephemeralin.parse;

import com.ephemeralin.data.RssEntry;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ephemeralin.parse.Constants.MIN_FEED_SIZE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
class RedditParserTest {

    @Test
    @DisplayName("Reddit prog")
    void parseRedditProg() {
        RedditParser parser = new RedditParser();
        List<RssEntry> list = parser.parse("https://www.reddit.com/r/programming/.rss?sort=hot");
        assertNotNull(list);
        assertTrue(list.size() > MIN_FEED_SIZE - 1);
        System.out.println(list.get(3).toString());
    }

    @Test
    @DisplayName("Reddit tech")
    void parseRedditTech() {
        RedditParser parser = new RedditParser();
        List<RssEntry> list = parser.parse("https://www.reddit.com/r/technology/.rss?sort=hot");
        assertNotNull(list);
        assertTrue(list.size() > MIN_FEED_SIZE - 1);
        System.out.println(list.get(3).toString());
    }
}