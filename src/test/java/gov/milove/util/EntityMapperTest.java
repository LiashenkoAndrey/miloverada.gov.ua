package gov.milove.util;


import gov.milove.domain.LinkBanner;
import org.junit.jupiter.api.Test;

import static gov.milove.util.EntityMapper.map;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityMapperTest {

    @Test
    public void mapTest() {
        LinkBanner banner = new LinkBanner();
        banner.setText("");
        banner.setUrl("https://docs.jboss.org");

        LinkBanner saved = new LinkBanner();
        saved.setUrl("https://old.url.org");
        saved.setText("old text");

        map(banner, saved)
                .mapEmptyString(false)
                .map();

        LinkBanner expected = LinkBanner.builder()
                .text("old text")
                .url("https://docs.jboss.org")
                .build();
        assertEquals(expected, saved);
    }


    @Test
    public void mapWithEmptyStringIgnoreTest() {
        LinkBanner banner = new LinkBanner();
        banner.setText("new text");
        banner.setUrl("");

        LinkBanner actual = new LinkBanner();
        actual.setUrl("https://current.url.org");
        actual.setText("old text");

        map(banner, actual)
                .mapEmptyString(false)
                .map();

        LinkBanner expected = LinkBanner.builder()
                .url("https://current.url.org")
                .text("new text")
                .build();

        assertEquals(expected, actual);

        LinkBanner expected2 = LinkBanner.builder()
                .url("")
                .text("new text")
                .build();

        LinkBanner actual2 = new LinkBanner();
        actual2.setUrl("https://current.url");
        actual2.setText("old text");

        map(banner, actual2)
                .mapEmptyString(true)
                .map();

        assertEquals(expected2, actual2);
    }


    @Test
    public void mapWithNullIgnoreTest() {
        LinkBanner banner = new LinkBanner();
        banner.setText(null);
        banner.setUrl("https://new.url.org");

        LinkBanner actual = new LinkBanner();
        actual.setUrl("https://old.url.org");
        actual.setText("old text");

        map(banner, actual)
                .mapNull(false)
                .map();

        LinkBanner expected = LinkBanner.builder()
                .url("https://new.url.org")
                .text("old text")
                .build();

        assertEquals(expected, actual);
    }

    @Test
    public void mapTest2() {
        LinkBanner newBanner = LinkBanner.builder()
                .text("new text")
                .url("")
                .build();

        LinkBanner oldBanner = LinkBanner.builder()
                .text("text")
                .url("https://current.url")
                .build();

        map(newBanner, oldBanner)
                .mapEmptyString(false)
                .map();

        LinkBanner expected = LinkBanner.builder()
                .text("new text")
                .url("https://current.url")
                .build();
        assertEquals(expected, oldBanner);
    }
}
