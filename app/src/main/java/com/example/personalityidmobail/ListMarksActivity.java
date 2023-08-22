package com.example.personalityidmobail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.personalityidmobail.adapters.ListMarkAdapter;
import com.example.personalityidmobail.adapters.ListMovingPupilAdapter;
import com.example.personalityidmobail.models.Mark;
import com.example.personalityidmobail.models.MovingPupil;
import com.example.personalityidmobail.models.Pupil;
import com.example.personalityidmobail.services.MarkService;
import com.example.personalityidmobail.services.MovingPupilService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListMarksActivity extends AppCompatActivity {

    Pupil pupil;

    ArrayList<Mark> items = new ArrayList<Mark>();
    ListMarkAdapter listMarkAdapter;
    int positionListLongClick = 0;

    Retrofit retrofit;
    MarkService markService;

    String search = "";

    public ListMarksActivity() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.105:5000")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        markService = retrofit.create(MarkService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_marks);
        Bundle arguments = getIntent().getExtras();
        pupil = (Pupil) arguments.getSerializable("pupil");
        if (search.equals("")) {
            listItems();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void listItems() {
        Call<List<Mark>> response = markService.listMarks(String.valueOf(pupil.id));
        response.enqueue(new Callback<List<Mark>>() {
            @Override
            public void onResponse(Call<List<Mark>> call, Response<List<Mark>> response) {
                try {
                    items = new ArrayList<Mark>();
                    for (Mark elem : response.body()) {
                        items.add(elem);
                        Log.d("Response", elem.id + " ");
                    }
                    createList();
                } catch (Exception e) {
                    Log.d("Error MovingPupil:", "Try again");
                }

            }

            @Override
            public void onFailure(Call<List<Mark>> call, Throwable t) {
                Log.e("Error", "Could not retrieve data//" + t);
            }
        });
    }

    public void findItems(String query) {
        System.out.println(query);
        Call<List<Mark>> response = markService.searchMarks(String.valueOf(pupil.id), query);
        response.enqueue(new Callback<List<Mark>>() {
            @Override
            public void onResponse(Call<List<Mark>> call, Response<List<Mark>> response) {
                try {
                    items = new ArrayList<Mark>();
                    for (Mark elem : response.body()) {
                        items.add(elem);
                        Log.d("Response", elem.id + " ");
                    }
                    createList();
                } catch (Exception e) {
                    Log.d("Error MovingPupil:", "Try again");
                }

            }

            @Override
            public void onFailure(Call<List<Mark>> call, Throwable t) {
                Log.e("Error", "Could not retrieve data//" + t);
            }
        });
    }

    public void createList() {
        listMarkAdapter = new ListMarkAdapter(this, items);
        ListView lvMain = (ListView) findViewById(R.id.list_mark);
        lvMain.setAdapter(listMarkAdapter);
        System.out.println("+++++++");
        listMarkAdapter.notifyDataSetChanged();
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
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();
        if (!search.equals("")) {
            searchView.onActionViewExpanded();
            searchView.setQuery(search, true);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search = query;
                findItems(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    search = "";
                    listItems();
                }
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("search", search);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        search = savedInstanceState.getString("search");
        System.out.println("------" + search);
        findItems(search);
    }
}