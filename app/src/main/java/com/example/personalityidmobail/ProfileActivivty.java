package com.example.personalityidmobail;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.personalityidmobail.models.AuthUserResponse;
import com.example.personalityidmobail.models.User;
import com.example.personalityidmobail.services.AuthService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivivty extends AppCompatActivity {

    AuthUserResponse authUserResponse;
    User user;

    Retrofit retrofit;
    AuthService authService;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN = "";
    public static final String APP_PREFERENCES_PASSWORD = "";
    SharedPreferences mSettings;

    public ProfileActivivty() {
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
        setContentView(R.layout.activity_profile);
        Bundle arguments = getIntent().getExtras();
        authUserResponse = (AuthUserResponse) arguments.getSerializable("user");
        if (authUserResponse.role.equals("Administrator")) {
            ((Button) findViewById(R.id.button_timetable)).setVisibility(View.GONE);
        }
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        findItems();
    }

    public void findItems() {
        Call<User> response = authService.profileUser(authUserResponse);
        System.out.println("ID" + authUserResponse.id + " " + authUserResponse.role);
        response.enqueue(new Callback<User>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    user = response.body();
                    createProfile();
                } catch (Exception e) {
                    Log.d("Error:", "Try again");
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Error", "Could not retrieve data//" + t);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String parseDateTime(String dateTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);

        DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = localDateTime.format(aFormatter);

        return  formattedDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createProfile() {
        ((TextView) findViewById(R.id.profile_id)).setText("ID:" + String.valueOf(user.id));
        ((TextView) findViewById(R.id.profile_name)).setText(user.name);
        ((TextView) findViewById(R.id.profile_role)).setText("Role: " + user.role);
        String date = parseDateTime(user.dateofbirth);
        ((TextView) findViewById(R.id.profile_dateofbirth)).setText("Date of birth: " + date);
    }

    public void logout() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_LOGIN, "");
        editor.putString(APP_PREFERENCES_PASSWORD, "");
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void createTimetable(View view) {
        Intent intent = new Intent(this, TimetableActivity.class);
        intent.putExtra("user", user);
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.menu_item_exit:
                logout();
                return true;

        }
        //headerView.setText(item.getTitle());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}