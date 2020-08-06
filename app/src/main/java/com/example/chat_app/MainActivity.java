package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chat_app.chat.ChatsActivity;
import com.example.chat_app.reterofit.LoginApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private LoginApi login;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        if (sharedPreferences.getString("userId", null) != null)
            openChats();


        Log.i("ChatApp", "request sent");
        Log.i("toke",Token());
        setContentView(R.layout.activity_main);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.13:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //button
        login = retrofit.create(LoginApi.class);
        Button sigUnpButton = (Button) findViewById(R.id.signUp);
        sigUnpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        Button logInButton = (Button) findViewById(R.id.login_button);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggingIn();
                //signUp();
            }
        });
    }

    private void signUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void loggingIn() {
        //Toast.makeText(MainActivity.this,"log in tapped",Toast.LENGTH_LONG);
        User user = new User(((EditText) findViewById(R.id.email)).getText().toString(), ((EditText) findViewById(R.id.password)).getText().toString(), "");
        Call<String> call = login.userLogin(user);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    Log.i("user", response.body().toString());
                    SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userId", response.body().toString());
                    editor.commit();
                    openChats();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Email or Passwrord", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("user", "Failed");
                Log.i("user", t.getMessage());
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });
        Log.i("user", "After");
    }

    private void openChats() {
        Intent intent = new Intent(MainActivity.this, ChatsActivity.class);
//        Log.i("Token",getToken());
        startActivity(intent);
        finish();
    }
    private String Token() {
        return FirebaseInstanceId.getInstance().getInstanceId().toString();
    }
}

