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
        assertNotNull(randomComic.getNum());
    }

    @Test
    void testLatest() {
        XkcdComic randomComic = client.latest();

        assertNotNull(randomComic);
        assertNotNull(randomComic.getNum());
    }

    @Test
    void testGetById() {
        XkcdComic comic = client.get(3);

        assertEquals("1", comic.getMonth());
        assertEquals("2006", comic.getYear());
        assertEquals("1", comic.getDay());
        assertEquals(3, comic.getNum());
        assertEquals("", comic.getLink());
        assertEquals("", comic.getNews());
        assertEquals("Island (sketch)", comic.getTitle());
        assertEquals("Island (sketch)", comic.getSafe_title());
        assertEquals("[[A sketch of an Island]]\n{{Alt:Hello, island}}", comic.getTranscript());
        assertEquals("Hello, island", comic.getAlt());
        assertEquals("https://imgs.xkcd.com/comics/island_color.jpg", comic.getImg());
    }

}
