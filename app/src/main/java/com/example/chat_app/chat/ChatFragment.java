package com.example.chat_app.chat;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chat_app.R;
import com.example.chat_app.chat.Chat;
import com.example.chat_app.chat.ChatAdapter;
import com.example.chat_app.reterofit.LoginApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChatFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DrawerLayout drawer=(DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        View v = inflater.inflate(R.layout.activity_chats_backup, container, false);
        Log.i("ChatApp", "Chat request sent");
        getActivity().setTitle("Chats");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.13:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        Log.i("ChatApp", "before shared");

        String id = sharedPreferences.getString("userId", "");
        Log.i("ChatApp", id);

        Call<List<Chat>> call = retrofit.create(LoginApi.class).getChats(id);
        Log.i("ChatApp", "after shared");

        call.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                Log.i("user", response.message());
                Log.i("user", response.toString());
                if (response.isSuccessful()) {
                    Log.i("user", "Success");
                    Toast.makeText(getActivity(), "Passed", Toast.LENGTH_LONG).show();
                    Log.i("user", "Before chat view");
                    final ListView chatView = (ListView) getView().findViewById(R.id.chatView2);
                    Log.i("user", "After chat view");
                    ArrayList<Chat> arrayList = new ArrayList<Chat>(response.body());
//                    ArrayList<String> ids = new ArrayList<>();
//                    for (int i = 0; i < arrayList.size(); i++) {
//                        ids.add(arrayList.get(i).friend);
//                    }

                    ChatAdapter arrayAdapter = new ChatAdapter(getActivity(), arrayList);
                    chatView.setAdapter(arrayAdapter);
                    chatView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Bundle bundle= new Bundle();
                            bundle.putString("name", ((Chat) chatView.getItemAtPosition(position)).friend);
                            bundle.putString("chatId", ((Chat) chatView.getItemAtPosition(position))._id);
                            bundle.putString("title",((Chat)chatView.getItemAtPosition(position)).friend);
                            Navigation.findNavController(view).navigate(R.id.action_chats_to_message,bundle);
                        }
                    });
                } else {
                    Log.i("user", "404");
                    Toast.makeText(getActivity(), "404", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Toast.makeText(getActivity(), "Failure", Toast.LENGTH_LONG).show();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "fab", Toast.LENGTH_LONG).show();
                Navigation.findNavController(view).navigate(R.id.action_chats_to_contacts);
            }
        });
        return v;
    }
}
