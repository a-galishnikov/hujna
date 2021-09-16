package ru.hujna.feature;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public interface UpdateMockProvider {
    default Update update(String text) {
        Long chatId = 1L;
        Integer msgId = 2;

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
