package ru.hujna.feature.huj;

import org.junit.jupiter.api.Test;
import ru.hujna.feature.UpdateMockProvider;
import ru.hujna.processor.matcher.Matcher;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HujnaMatcherTest implements UpdateMockProvider {

    HujnaConfig config = new HujnaConfig();
    Matcher matcher = config.hujnaMatcher();

    @Test
    void russianAaaLetters() {
        happyMatchCase("АаааАаа");
    }

    @Test
    void russianAaaLettersAndSpecial() {
        happyMatchCase("АаааАаа!!..");
    }

    @Test
    void englishAaaLetters() {
        happyMatchCase("aaaAAaA");
    }

    @Test
    void englishAaaLettersAndSpecial() {
        happyMatchCase("aaaAAaA?!%$");
    }

    @Test
    void russianOtherLetters() {
        unhappyMatchCase("А что он умеет?");
    }

    @Test
    void russianAga() {
        unhappyMatchCase("Ага");
    }

    @Test
    void englishOtherLetters() {
        unhappyMatchCase("And you think this is smart..");
    }

    private void happyMatchCase(String text) {
        assertTrue(matcher.match(update(text)));
    }

    private void unhappyMatchCase(String text) {
        assertFalse(matcher.match(update(text)));
    }

}
