package ru.hujna.feature.huj;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.hujna.feature.UpdateMockProvider;
import ru.hujna.processor.handler.Handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.util.Assert.isInstanceOf;

class HujnaHandlerTest implements UpdateMockProvider {

    HujnaConfig config = new HujnaConfig();
    Handler handler = config.hujnaHandler();

    @Test
    void testHandler() {
        var update = update("whatever");

        var botMethods = handler.handle(update);

        assertEquals(1, botMethods.size());

        var botMethod = botMethods.get(0);
        isInstanceOf(SendMessage.class, botMethod);

        var sendMessage = (SendMessage) botMethod;
        assertEquals(update.getMessage().getChatId().toString(), sendMessage.getChatId());
        assertEquals(update.getMessage().getMessageId(), sendMessage.getReplyToMessageId());
        assertEquals(HujnaHandler.HUJNA, sendMessage.getText());
    }

}
