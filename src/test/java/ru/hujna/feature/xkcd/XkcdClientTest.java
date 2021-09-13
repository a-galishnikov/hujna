package ru.hujna.feature.xkcd;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("xkcd-integration")
public class XkcdClientTest {

    XkcdClient client = new XkcdClientImpl();

    @Test
    void testRandom() {
        XkcdComic randomComic = client.random();

        assertNotNull(randomComic);
        assertNotNull(randomComic.num());
    }

    @Test
    void testLatest() {
        XkcdComic randomComic = client.latest();

        assertNotNull(randomComic);
        assertNotNull(randomComic.num());
    }

    @Test
    void testGetById() {
        XkcdComic comic = client.get(3);

        assertEquals("1", comic.month());
        assertEquals("2006", comic.year());
        assertEquals("1", comic.day());
        assertEquals(3, comic.num());
        assertEquals("", comic.link());
        assertEquals("", comic.news());
        assertEquals("Island (sketch)", comic.title());
        assertEquals("Island (sketch)", comic.safe_title());
        assertEquals("[[A sketch of an Island]]\n{{Alt:Hello, island}}", comic.transcript());
        assertEquals("Hello, island", comic.alt());
        assertEquals("https://imgs.xkcd.com/comics/island_color.jpg", comic.img());
    }

}
