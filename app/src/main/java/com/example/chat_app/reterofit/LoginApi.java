package com.example.chat_app.reterofit;

import com.example.chat_app.chat.Chat;
import com.example.chat_app.message.Message;
import com.example.chat_app.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginApi
{
        @GET("chats/{id}")
        Call<List<Chat>>  getChats(@Path("id") String id );
        @GET("getMessages/{chatId}")
        Call<List<Message>> getMessages(@Path("chatId") String chatId);
        @POST("login")
        Call<String> userLogin(@Body User log);
        @POST("signIn")
        Call<String> userSignIn(@Body User user);
        @POST("sendMessage")
        Call<Message> sendMessage(@Body Message message);
}
