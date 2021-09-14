package ru.hujna.feature.huj;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.processor.handler.Handler;
import ru.hujna.processor.matcher.Matcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.isInstanceOf;

class HujnaProcessorTest {

    HujnaConfig config = new HujnaConfig();
    Handler handler = config.hujnaHandler();
    Matcher matcher = config.hujnaMatcher();

    Long chatId = 1L;
    Integer msgId = 2;
    String msgText = HujnaHandler.HUJNA;

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

    @Test
    void testHandler() {
        var text = "whatever";
        var res = handler.handle(update(text));
        assertEquals(1, res.size());

        var method = res.get(0);
        isInstanceOf(SendMessage.class, method);

        var sendMessage = (SendMessage) method;
        assertEquals(chatId.toString(), sendMessage.getChatId());
        assertEquals(msgId, sendMessage.getReplyToMessageId());
        assertEquals(msgText, sendMessage.getText());
    }

    private void happyMatchCase(String text) {
        assertTrue(matcher.match(update(text)));
    }

    private void unhappyMatchCase(String text) {
        assertFalse(matcher.match(update(text)));
    }

    private Update update(String text) {
        var message = mock(Message.class);
        when(message.getMessageId()).thenReturn(msgId);
        when(message.getChatId()).thenReturn(chatId);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(text);

        var update = mock(Update.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        return update;
    }

}
