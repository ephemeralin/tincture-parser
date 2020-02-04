package com.ephemeralin.crawl;

import com.ephemeralin.data.RssEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DevbyParserTest {

    @Test
    @DisplayName("Dev.by parser test")
    void parseWylsa() {
        DevbyParser parser = new DevbyParser();
        List<RssEntry> list = parser.parse("https://dev.by/rss");
        assertNotNull(list);
        assertTrue(list.size() > 9);
        System.out.println(list.get(0).toString());
//        assertEquals(30, list.size(), "List size should equal 2");

    }
}