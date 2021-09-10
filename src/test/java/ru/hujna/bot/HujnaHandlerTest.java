package ru.hujna.bot;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.isInstanceOf;

class HujnaHandlerTest {

    UpdateHandler handler = new HujnaHandler();

    Long chatId = 1L;
    Integer msgId = 2;
    String msgText = HujnaHandler.HUJNA;

    @Test
    void russianAaaLetters() {
        happyCase("АаааАаа");
    }

    @Test
    void russianAaaLettersAndSpecial() {
        happyCase("АаааАаа!!..");
    }

    @Test
    void englishAaaLetters() {
        happyCase("aaaAAaA");
    }

    @Test
    void englishAaaLettersAndSpecial() {
        happyCase("aaaAAaA?!%$");
    }

    @Test
    void russianOtherLetters() {
        unhappyCase("А что он умеет?");
    }

    @Test
    void russianAga() {
        unhappyCase("Ага");
    }

    @Test
    void englishOtherLetters() {
        unhappyCase("And you think this is smart..");
    }

    private void happyCase(String text) {
        var res = handler.handle(update(text));
        assertTrue(res.isPresent());

        var method = res.get();
        isInstanceOf(SendMessage.class, method);

        var sendMessage = (SendMessage) method;
        assertEquals(chatId.toString(), sendMessage.getChatId());
        assertEquals(msgId, sendMessage.getReplyToMessageId());
        assertEquals(msgText, sendMessage.getText());
    }

    private void unhappyCase(String text) {
        var res = handler.handle(update(text));
        assertTrue(res.isEmpty());
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
