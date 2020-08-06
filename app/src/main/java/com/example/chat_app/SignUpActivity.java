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

public class SignUpActivity extends AppCompatActivity {

    private LoginApi signIn_reterofit;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.13:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        signIn_reterofit = retrofit.create(LoginApi.class);
        Button button = (Button) findViewById(R.id.signIn_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) findViewById(R.id.signIn_email)).getText().toString();
                String password = ((EditText) findViewById(R.id.signIn_password)).getText().toString();
                String conform_password = ((EditText) findViewById(R.id.signIn_confirm_password)).getText().toString();
                if (!password.equals(conform_password)) {
                    Toast.makeText(SignUpActivity.this, "Password didn't match", Toast.LENGTH_LONG).show();
                } else {
                    signIn(email, password);
                }
            }
        });
    }

    public void signIn(String email, String password) {
        User user = new User(email, password,Token());
        Call<String> call = signIn_reterofit.userSignIn(user);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(SignUpActivity.this, ChatsActivity.class);
                    SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userId", response.body().toString());
                    editor.putString("token", Token());
                    editor.commit();
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Failure", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String Token() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("firebase", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        //Log.d("firebase", msg);
                        //Toast.makeText(ChatsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        return token;
    }
}
