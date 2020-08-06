package com.example.chat_app.message;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chat_app.R;
import com.example.chat_app.chat.ChatsActivity;
import com.example.chat_app.message.Message;
import com.example.chat_app.message.MessageAdapter;
import com.example.chat_app.reterofit.LoginApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.client.Manager.Options;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MessageFragment extends Fragment {

    MessageAdapter messageAdapter;
    ListView messageView;
    Message message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DrawerLayout drawer=(DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        View v= inflater.inflate(R.layout.activity_message_view, container, false);
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        final String userId=sharedPreferences.getString("userId","");
        //Intent intent=getActivity().getIntent();
        String Name = getArguments().getString("name");
        final String chatId = getArguments().getString("chatId");
        Log.i("chatId", chatId);
        getActivity().setTitle(Name);
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.13:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<List<Message>> call=retrofit.create(LoginApi.class).getMessages(chatId);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                Log.i("response",response.message().toString());
                if(response.isSuccessful())
                {
                    messageView=(ListView)getView().findViewById(R.id.messages_view);
                    ArrayList<Message> messages= new ArrayList<>(response.body());
                    messageAdapter = new MessageAdapter(getActivity(),messages,userId);
                    messageView.setAdapter(messageAdapter);
                    Log.i("size Of adapter",messageAdapter.getCount()+" ");
                    messageView.scrollListBy(messageAdapter.getCount()-1);
                }
            }
            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.i("response",t.getMessage());
            }
        });
        Socket mSocket=null;
        Log.i("socket","above");
        try {
            IO.Options options= new IO.Options();
            options.query="UserID="+userId;
            mSocket = IO.socket("http://192.168.1.13:3000", options);
            Log.i("socket","connected");
            mSocket.on("message", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    Log.i("call","done");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject data=(JSONObject)args[0];
                            try {
                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                                Date date = format.parse((String)data.get("date"));
                                Message message= new Message(data.getString("content"),data.getString("sender"),data.getString("chatId"), date);
                                messageAdapter.add(message);
                                messageAdapter.notifyDataSetChanged();
                                messageView.smoothScrollToPosition(messageAdapter.getCount()-1);
                                //Toast.makeText(getActivity(),"message",Toast.LENGTH_LONG).show();
                                Log.i("message", "added");

                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            mSocket.connect();
        } catch (URISyntaxException e) {
            Log.i("error",e.getMessage());
        }


        ImageView sendButton=(ImageView)v.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesageContent=((EditText)getView().findViewById(R.id.messageContent)).getText().toString();
                Call<Message> messageCall=retrofit.create(LoginApi.class).sendMessage(new Message(mesageContent,userId,chatId,new Date()));
                messageCall.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        Log.i("response",response.message().toString());
                        message=response.body();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (messageAdapter) {
                                    messageAdapter.add(message);
                                    messageAdapter.notifyDataSetChanged();
                                    messageView.smoothScrollToPosition(messageAdapter.getCount()-1);

                                    Log.i("response","added");
                                }}
                        });
                    }
                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Log.i("message result","failure");
                    }
                });
                ((EditText)(getView().findViewById(R.id.messageContent))).setText("");
            }
        });
        return v;
    }
}
