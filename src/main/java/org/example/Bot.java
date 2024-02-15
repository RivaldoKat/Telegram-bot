package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Bot extends TelegramLongPollingBot {
    private boolean screaming = false;
    InlineKeyboardButton next = InlineKeyboardButton.builder().text("Next").callbackData("next").build();
    InlineKeyboardButton back = InlineKeyboardButton.builder().text("Back").callbackData("back").build();
    InlineKeyboardButton url = InlineKeyboardButton.builder().text("My GitHub").url("https://github.com/RivaldoKat").build();
    private final InlineKeyboardMarkup keyboardM1 = InlineKeyboardMarkup.builder().keyboardRow(List.of(next)).build();
    private final InlineKeyboardMarkup keyboardM2 = InlineKeyboardMarkup.builder().keyboardRow(List.of(back)).keyboardRow(List.of(url)).build();


    @Override
    public String getBotUsername(){
        return "SimplePracticeTeleBot";
    }

    @Override
    public String getBotToken(){
        Dotenv env = new DotenvBuilder().load();
        return env.get("token");
    }

    @Override
    public void onUpdateReceived(Update update){

        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();
        var txt = msg.getText();

        copyMessage(id, msg.getMessageId());

        if(msg.isCommand()){
            switch(txt){
                case "/start" ->{
                    try{
                        File readMyfile = new File("/home/rivaldo/Documents/Coding/SimpleTeleBot/src/resume");
                        Scanner myReder = new Scanner(readMyfile);
                        while(myReder.hasNext()){
                            String data = myReder.nextLine();
                            sendText(id,data);
                        }
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                case "/scream" -> {

                    screaming = true;
                }
                case "/whisper" -> screaming = false;
                case "/menu" -> {
                    sendMenu(id, "<b>Menu 1</b>", keyboardM1);
                    sendMenu(id, "<b>Menu 2</b>", keyboardM2);
                }
            }
            return;
        }
    }


    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder().chatId(who.toString()).text(what).build();
        try{
            execute(sm);
        }catch(TelegramApiException e){
            throw new RuntimeException(e);
        }
    }
    public void copyMessage(Long who, Integer msgId){
        CopyMessage cm = CopyMessage.builder()
                .fromChatId(who.toString())
                .chatId(who.toString())
                .messageId(msgId).build();
        try{
            execute(cm);
        }catch(TelegramApiException e){
            throw new RuntimeException(e);
        }
    }
    public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb){
        SendMessage sm = SendMessage.builder().chatId(who.toString())
                .parseMode("HTML").text(txt).replyMarkup(kb).build();
        try{
            execute(sm);
        }catch(TelegramApiException e){
            throw new RuntimeException(e);
        }

    }

}
