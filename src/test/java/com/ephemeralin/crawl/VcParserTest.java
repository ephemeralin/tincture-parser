package com.ephemeralin.crawl;

import com.ephemeralin.data.RssEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VcParserTest {

    @Test
    @DisplayName("Vc parser test")
    void parse() {
        VcParser parser = new VcParser();
        List<RssEntry> list = parser.parse("https://vc.ru/rss/all/");
        assertNotNull(list);
        assertTrue(list.size() != 0, "List must not be empty");
        assertTrue(list.size() > 9);
        System.out.println(list.get(0).toString());
    }
}