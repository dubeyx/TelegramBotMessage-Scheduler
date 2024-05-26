package com.courpedia.crackjeepro.Services;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import software.amazon.awssdk.services.s3.internal.resource.S3AccessPointBuilder;


public class Bot extends TelegramLongPollingBot {
    private int x=6;
    final static private String BotToken = "6985266350:AAGq_LIy4gYa_YW4Q3QJGA22A3cKqsmQgTI";

    final static private String BotName = "CrackJeePro";

    private S3ImageProcessor s3ImageProcessor;

    @Override
    public String getBotUsername() {
        return BotName;
    }

    @Override
    public String getBotToken() {
        return BotToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

        System.out.println("New Update here : " + update);

        if (update.hasChannelPost()) {

            ProcessMessage.channelList.put(update.getChannelPost().getChat().getUserName(), update.getChannelPost().getChatId());


        } else {
            String msg = update.getMessage().getText();
            if (msg.length() > 0 && msg.charAt(0) == '/') {
                ProcessMessage.parseCommand(update.getMessage().getChatId(), msg);
            }
        }

    }

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }



    public void sendMessageToChannel(String chatId, InputFile file, String name) {
        SendPhoto sendPhoto = new SendPhoto(chatId.toString(),file);
        sendPhoto.setCaption("Jee Mains Previous Year Question Paper "+name);
        x++;
        try {
            execute(sendPhoto); // Sending the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}