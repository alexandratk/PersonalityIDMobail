package com.example.personalityidmobail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.personalityidmobail.models.AuthUserRequest;
import com.example.personalityidmobail.models.AuthUserResponse;
import com.example.personalityidmobail.services.AuthService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    EditText mailEditText;
    EditText passwordEditText;

    Retrofit retrofit;
    AuthService authService;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN = "";
    public static final String APP_PREFERENCES_PASSWORD = "";
    SharedPreferences mSettings;

    public MainActivity() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.105:5000")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        authService = retrofit.create(AuthService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        checkPreferences();

        mailEditText = findViewById(R.id.editTextLogin);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.buttonLogin);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = mailEditText.getText().toString();
                String password = mailEditText.getText().toString();

                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_LOGIN, mail);
                editor.putString(APP_PREFERENCES_PASSWORD, password);
                editor.apply();

                auth(mail, password);

            }
        });
    }

    public void auth(String mail, String password) {
        AuthUserRequest aur = new AuthUserRequest(mail, password);
        Call<AuthUserResponse> response = authService.authUser(aur);
        response.enqueue(new Callback<AuthUserResponse>() {
            @Override
            public void onResponse(Call<AuthUserResponse> call, Response<AuthUserResponse> response) {
                try {
                    AuthUserResponse user = response.body();
                    Log.d("Response:", user.id + " " + user.role);
                    if (user.role.equals("Parent")) {
                        changeActivityParent(user);
                    } else {
                        changeActivity(user);
                    }
                } catch (Exception e) {
                    Log.d("Error:", "Try again");
                }

            }

            @Override
            public void onFailure(Call<AuthUserResponse> call, Throwable t) {
                Log.e("Error", "Could not retrieve data//" + t);
            }
        });
    }

    public boolean checkPreferences() {
        if (mSettings.contains(APP_PREFERENCES_LOGIN) && mSettings.contains(APP_PREFERENCES_PASSWORD)) {
            String mail = mSettings.getString(APP_PREFERENCES_LOGIN, "");
            String password = mSettings.getString(APP_PREFERENCES_PASSWORD, "");
            auth(mail, password);
            return true;
        }
        return false;
    }

    public void changeActivityParent(AuthUserResponse user) {
        Intent intent = new Intent(this, ListPupilActivity.class);
        intent.putExtra("user", user);
        startActivityForResult(intent, 1);
    }

    public void changeActivity(AuthUserResponse user) {
        Intent intent = new Intent(this, ProfileActivivty.class);
        intent.putExtra("user", user);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_LOGIN, "");
        editor.putString(APP_PREFERENCES_PASSWORD, "");
        editor.apply();
    }
}