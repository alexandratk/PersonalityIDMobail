package com.example.personalityidmobail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.personalityidmobail.adapters.ListMovingPupilAdapter;
import com.example.personalityidmobail.adapters.ListPupilAdapter;
import com.example.personalityidmobail.models.AuthUserResponse;
import com.example.personalityidmobail.models.MovingPupil;
import com.example.personalityidmobail.models.Pupil;
import com.example.personalityidmobail.services.MovingPupilService;
import com.example.personalityidmobail.services.PupilService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class ListMovingPupilActivity extends AppCompatActivity {

    Pupil pupil;

    ArrayList<MovingPupil> items = new ArrayList<MovingPupil>();
    ListMovingPupilAdapter listMovingPupilAdapter;
    int positionListLongClick = 0;

    Retrofit retrofit;
    MovingPupilService movingPupilService;

    public ListMovingPupilActivity() {
        System.out.println("this constructor was started");
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.105:5000")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        movingPupilService = retrofit.create(MovingPupilService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_moving_pupil);
        System.out.println("this activity was started");
        Bundle arguments = getIntent().getExtras();
        pupil = (Pupil) arguments.getSerializable("pupil");
        findItems();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void findItems() {
        Call<List<MovingPupil>> response = movingPupilService.listMovingPupil(String.valueOf(pupil.id));
        response.enqueue(new Callback<List<MovingPupil>>() {
            @Override
            public void onResponse(Call<List<MovingPupil>> call, Response<List<MovingPupil>> response) {
                System.out.println("8888888" + String.valueOf(response.body()));
                try {
                    if (response.body() != null) {
                        for (MovingPupil elem : response.body()) {
                            items.add(elem);
                            Log.d("Response", elem.id + " ");
                        }
                        int c = 1;
                        for (int i = items.size() - 1; i >= 0; i--) {
                            if (c > 0) {
                                items.get(i).message = "in";
                            } else {
                                items.get(i).message = "out";
                            }
                            c = c * (-1);
                        }
                    }
                    createList();
                } catch (Exception e) {
                    Log.d("Error MovingPupil:", "Try again");
                }

            }

            @Override
            public void onFailure(Call<List<MovingPupil>> call, Throwable t) {
                Log.e("Error", "Could not retrieve data//" + t);
            }
        });
    }

    public void createList() {
        listMovingPupilAdapter = new ListMovingPupilAdapter(this, items);
        ListView lvMain = (ListView) findViewById(R.id.list_moving_pupil);
        lvMain.setAdapter(listMovingPupilAdapter);
        System.out.println("+++++++");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_menu, menu);
        return true;
    }
}