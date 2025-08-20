package ru.service.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.service.model.ProductDTO;
import ru.service.service.DynamicRuleService;

import java.util.List;

@Component
public class RecommendationBot implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final DynamicRuleService service;

    @Value("${telegram.bot.token}")
    private String token;


    public RecommendationBot(TelegramBot telegramBot, DynamicRuleService service) {
        this.telegramBot = telegramBot;
        this.service = service;
    }

    @PostConstruct
    public void startBot() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        list.forEach(update -> {
            if (update.message() == null || update.message().text() == null) return;

            Long id = update.message().chat().id();
            final String command = update.message().text();

            if (command.startsWith("/recommend")) {
                String username = command.substring("/recommend".length()).trim();
                if (username.length() < 5) {
                    sendResponse(id,
                            "❌ Имя пользователя должно содержать минимум 5 символов\n" +
                                    "Пример: /recommend клиент12345");
                    return;
                }
                final String userName = command.substring(11).trim();
                sendRecommendations(id, userName);
            } else {
                sendResponse(id, "Используйте ¨/recommend username¨ для получения ваших рекоммендаций.");
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendRecommendations(Long chatId, String username) {
        try {
            List<ProductDTO> recommendationsForUser = service.getRecommendationsForUser(username);
            if (recommendationsForUser.isEmpty()) {
                sendResponse(chatId, "Пользователь «" + username + "» не найден, или нет рекомендаций");
            } else {
                StringBuilder response = new StringBuilder("*Рекомендации для " + username + "*:\n\n");
                recommendationsForUser.forEach(p ->
                        response.append("🔹 *").append(p.getName()).append("*: ").append(p.getText()).append("\n"));
                sendResponse(chatId, response.toString());
            }
        } catch (Exception e) {
            sendResponse(chatId, "Ошибка при обработке запроса. Попробуйте позже.");
        }

    }

    private void sendResponse(Long chatId, String message) {
        telegramBot.execute(new SendMessage(chatId, message).parseMode(ParseMode.Markdown));
    }

}
