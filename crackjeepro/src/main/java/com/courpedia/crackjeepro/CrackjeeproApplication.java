package com.courpedia.crackjeepro;

import com.courpedia.crackjeepro.Services.Bot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class CrackjeeproApplication {

	public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(CrackjeeproApplication.class, args);
		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		botsApi.registerBot(new Bot());
	}

}
