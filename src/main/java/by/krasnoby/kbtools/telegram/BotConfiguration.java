package by.krasnoby.kbtools.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class BotConfiguration {

    @Autowired
    BotTokenHolder botTokenHolder;

    @Bean
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(botTokenHolder.getBotToken());
    }

}