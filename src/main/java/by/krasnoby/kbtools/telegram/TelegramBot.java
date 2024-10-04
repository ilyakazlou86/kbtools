package by.krasnoby.kbtools.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final String botToken;
    private final UserService userService;

    public TelegramBot(TelegramClient telegramClient, @Value("${bot.token}") String botToken, UserService userService) {
        this.telegramClient = telegramClient;
        this.botToken = botToken;
        this.userService = userService;
    }

    @Override
    public void consume(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            var chatId = update.getMessage().getChatId();
            var userName = update.getMessage().getFrom().getUserName();
            var messageText = update.getMessage().getText();

            switch (messageText) {
                case "/start" -> doStart(chatId, userName);
            }
        }
    }

    private void sendMessage(Long chatId, String messageText) {
        try {
            SendMessage message = SendMessage.builder().chatId(chatId).text(messageText).build();
            telegramClient.execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending a message to {}: {}", chatId, e.getMessage());
        }
    }

    private void doStart(Long chatId, String userName) {
        TelegramUser newUser = TelegramUser.builder().chatId(chatId).userName(userName).build();
        if (userService.registerNewUser(newUser)) {
            sendMessage(chatId, "You're successfully registered!");
        } else {
            sendMessage(chatId, "You're already registered!");
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.info("Registered bot running state is: {}", botSession.isRunning());
    }

}