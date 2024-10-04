package by.krasnoby.kbtools.telegram;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<TelegramUser, Long> {
}