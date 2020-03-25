package com.ephemeralin.parse;

import com.ephemeralin.data.RssEntry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConcurrentTest {

    @Test
    void test() {
        GenericParser parser = new GenericParser();
        List<RssEntry> list = parser.parse("https://habr.com/ru/rss/best/daily/");
        assertNotNull(list);

        
    }
}
