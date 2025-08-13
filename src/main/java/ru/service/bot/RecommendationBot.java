package ru.service.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.service.service.DynamicRuleService;

@Component
public class RecommendationBot {
private final TelegramBot telegramBot;
private final DynamicRuleService service;

@Value("${telegram.bot.token}")
private String token;


    public RecommendationBot(DynamicRuleService service) {
        this.telegramBot = new TelegramBot(token);
        this.service = service;
    }

    public void handleUpdate(Update update){
        if (update.message() != null) {
            final Message message = update.message();
            final String command = message.text();
            if (command.startsWith("/recommend")) {
                final String userName = command.substring(11);
                sendRecommendations(message.chat().id(), userName);
            } else {
                sendResponse(message.chat().id(), "Используйте ¨/recommend username¨ для получения ваших рекоммендаций.");
            }
        }
    }

    private void sendRecommendations(Long chatId, String userName){
    service
    }

    private void sendResponse(Long chatId, String message){
            try {telegramBot.execute(new SendMessage(chatId, message));
            } catch (RuntimeException e) {
               e.printStackTrace();
            }
    }


}
