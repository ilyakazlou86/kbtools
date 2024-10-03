package by.krasnoby.kbtools.telegram;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class BotTokenHolder {

    @Value("${bot.token}")
    private String botToken;

}