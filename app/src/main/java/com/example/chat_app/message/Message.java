package com.example.chat_app.message;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Message {
    @SerializedName("content")
    private String content;
    @SerializedName("date")
    private Date date;
    @SerializedName("sender")
    private String senderId;
    @SerializedName("chatId")
    private String chatId;
    public Message( String content, String senderId,String chatId,Date date) {
        this.content = content;
        this.senderId=senderId;
        this.chatId=chatId;
        this.date=date;
    }

    public Date getDate() {
        return date;
    }

    public String getChatId() {
        return chatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
