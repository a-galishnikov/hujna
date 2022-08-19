package ru.hujna.feature.xo.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.hujna.feature.xo.parse.JoinParser;


class GameUpdateTest {

    @Test
    @DisplayName("Проверка корректности конструктора.")
    void shouldSetFields() {
        Update update = getUpdate();
        new GameUpdate(update, new JoinParser());
    }

    @Test
    @DisplayName("Проверка что упадет при пустых полях")
    void shouldThrowNpe() {
        var update = new Update();
        var parser = new JoinParser();
        Executable executable = () -> new GameUpdate(update, parser);
        Assertions.assertThrows(NullPointerException.class,
                executable
        );
    }

    private Update getUpdate() {
        var opponent = new User();
        opponent.setId(1L);
        var chat = new Chat();
        chat.setId(1L);
        chat.setType("group");
        var message = new Message();
        message.setChat(chat);
        message.setText("message");
        message.setMessageId(1);
        var data = "messageId:1";
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setMessage(message);
        callbackQuery.setData(data);
        callbackQuery.setFrom(opponent);
        Update update = new Update();
        update.setCallbackQuery(callbackQuery);
        return update;
    }
}