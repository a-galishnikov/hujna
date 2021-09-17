package ru.hujna.feature.xo.model;

public record Key(Long chatId, Integer messageId) {

    public static Key of(Game game) {
        return of(game.getChatId(), game.getMessageId());
    }

    public static Key of(Long chatId, Integer messageId) {
        return new Key(chatId, messageId);
    }
}
