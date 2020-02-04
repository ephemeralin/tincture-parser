package com.ephemeralin.crawl;

import com.ephemeralin.data.RssEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TechrocksParserTest {

    @Test
    @DisplayName("Techrocks parser test")
    void parse() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://techrocks.ru/feed/");
        assertNotNull(list);
        assertTrue(list.size() > 9);
        System.out.println(list.get(0).toString());
//        assertEquals(30, list.size(), "List size should equal 2");

    }
}