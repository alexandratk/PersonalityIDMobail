package com.example.personalityidmobail;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.personalityidmobail.adapters.ListMarkAdapter;
import com.example.personalityidmobail.adapters.ListTimetableAdapter;
import com.example.personalityidmobail.models.Lesson;
import com.example.personalityidmobail.models.Mark;
import com.example.personalityidmobail.models.Pupil;
import com.example.personalityidmobail.models.User;
import com.example.personalityidmobail.services.LessonService;
import com.example.personalityidmobail.services.MarkService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TimetableActivity extends AppCompatActivity {

    User user;

    ArrayList<Lesson> items = new ArrayList<Lesson>();
    ListTimetableAdapter listTimetableAdapter;
    int positionListLongClick = 0;

    Retrofit retrofit;
    LessonService lessonService;

    public TimetableActivity() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.105:5000")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        lessonService = retrofit.create(LessonService.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        Bundle arguments = getIntent().getExtras();
        user = (User) arguments.getSerializable("user");
        listItems();
        ((TextView) findViewById(R.id.timetable_date)).setText(String.valueOf(LocalDate.now()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void listItems() {
        Call<List<Lesson>> response;
        if (user.role.equals("Teacher")) {
            System.out.println("teacher");
            response = lessonService.listLessonTeacher(String.valueOf(user.id));
        } else {
            response = lessonService.listLessonPupil(String.valueOf(user.id));
        }
        response.enqueue(new Callback<List<Lesson>>() {
            @Override
            public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                try {
                    items = new ArrayList<Lesson>();
                    for (Lesson elem : response.body()) {
                        items.add(elem);
                        Log.d("Response", elem.id + " ");
                    }
                    createList();
                } catch (Exception e) {
                    Log.d("Error Lesson:", "Try again");
                }

            }

            @Override
            public void onFailure(Call<List<Lesson>> call, Throwable t) {
                Log.e("Error", "Could not retrieve data//" + t);
            }
        });
    }

    public void createList() {
        listTimetableAdapter = new ListTimetableAdapter(this, items);
        ListView lvMain = (ListView) findViewById(R.id.list_timetable);
        lvMain.setAdapter(listTimetableAdapter);
        listTimetableAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                finish();
                return true;

        }
        //headerView.setText(item.getTitle());
        return super.onOptionsItemSelected(item);
    }
}