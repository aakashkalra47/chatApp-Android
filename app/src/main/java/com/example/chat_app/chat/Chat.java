package com.example.chat_app.chat;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Chat {
    @SerializedName("friend")
    public String friend;

    @SerializedName("_id")
    public String _id;


    public Chat (String friend) {
        this.friend = friend;
    }
}
