package by.krasnoby.kbtools.telegram;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerNewUser(TelegramUser newUser) {
        if (!userRepository.existsById(newUser.getChatId())) {
            userRepository.save(TelegramUser.builder().chatId(newUser.getChatId()).userName(newUser.getUserName()).build());
            return true;
        }
        return false;
    }

}