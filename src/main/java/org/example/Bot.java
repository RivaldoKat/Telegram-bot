package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {

    private boolean screaming = false;
    @Override
    public String getBotUsername(){
        return "SimplePracticeTeleBot";
    }

    @Override
    public String getBotToken(){
        return "";
    }

    @Override
    public void onUpdateReceived(Update update){
        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();
        sendText(id, msg.getText());

        if(screaming){
            scream(id, update.getMessage());
        }else{
            copyMessage(id, msg.getMessageId());
        }
        if(msg.isCommand()){
            if(msg.getText().equals("/scream")){
                screaming = true;
            }
            else if(msg.getText().equals("/whisper")){
                screaming = false;
            }
            return;
        }
    }

    private void scream(Long id, Message msg){
        if(msg.hasText()){
            sendText(id, msg.getText().toUpperCase());
        }
        else{
            copyMessage(id, msg.getMessageId());
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
}
