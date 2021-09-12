package ru.hujna.feature.huj;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.bot.BotConfig;
import ru.hujna.feature.config.ProcessorConfig;
import ru.hujna.processor.Processor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.isInstanceOf;

class HujnaProcessorTest {

    ProcessorConfig config = new ProcessorConfig(new BotConfig("@bot", "123"));
    Processor proc = config.hujnaProcessor();

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
        var res = proc.handle(update(text));
        assertEquals(1, res.size());

        var method = res.get(0);
        isInstanceOf(SendMessage.class, method);

        var sendMessage = (SendMessage) method;
        assertEquals(chatId.toString(), sendMessage.getChatId());
        assertEquals(msgId, sendMessage.getReplyToMessageId());
        assertEquals(msgText, sendMessage.getText());
    }

    private void happyMatchCase(String text) {
        assertTrue(proc.match(update(text)));
    }

    private void unhappyMatchCase(String text) {
        assertFalse(proc.match(update(text)));
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
