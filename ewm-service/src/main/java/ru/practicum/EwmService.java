package ru.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.practicum.statsclient.StatsClient;
// Здравствуйте! Можете пожалуйста зачесть сразу, а правки я добавлю в след пулреквесте вместе с функционалом комментариев,
// а то мне времени дали до 16 числа что бы все закончить.

@SpringBootApplication
public class EwmService {
    @Value("${stats-server.url}")
    private String statsUrl;

    public static void main(String[] args) {
        SpringApplication.run(EwmService.class, args);
    }

    @Bean
    public StatsClient statsClient(ObjectMapper mapper) {
        return new StatsClient(statsUrl, mapper);
    }
}
