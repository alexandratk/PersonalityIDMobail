package com.example.personalityidmobail;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.example.personalityidmobail.adapters.ListPupilAdapter;
import com.example.personalityidmobail.models.AuthUserResponse;
import com.example.personalityidmobail.models.Pupil;
import com.example.personalityidmobail.models.User;
import com.example.personalityidmobail.services.AuthService;
import com.example.personalityidmobail.services.PupilService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ListPupilActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN = "";
    public static final String APP_PREFERENCES_PASSWORD = "";
    SharedPreferences mSettings;

    Intent intentService;
    // Don't attempt to unbind from the service unless the client has received some
    // information about the service's state.
    private boolean mShouldUnbind;

    // To invoke the bound service, first make sure that this value
    // is not null.
    private LocalService mBoundService;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mBoundService = ((LocalService.LocalBinder)service).getService();

            // Tell the user about this for our demo.
            Toast.makeText(ListPupilActivity.this, R.string.local_service_connected,
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;
            Toast.makeText(ListPupilActivity.this, R.string.local_service_disconnected,
                    Toast.LENGTH_SHORT).show();
        }
    };



    AuthUserResponse authUserResponse;
    User user;

    ArrayList<Pupil> items = new ArrayList<Pupil>();
    ListPupilAdapter listPupilAdapter;
    int positionListLongClick = 0;

    Retrofit retrofit;
    PupilService pupilService;
    AuthService authService;

    public ListPupilActivity() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.105:5000")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        pupilService = retrofit.create(PupilService.class);
        authService = retrofit.create(AuthService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pupil);
        Bundle arguments = getIntent().getExtras();
        authUserResponse = (AuthUserResponse) arguments.getSerializable("user");
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        findItems();
    }

    public void findItems() {
        Call<List<Pupil>> response = pupilService.listPupil(String.valueOf(authUserResponse.id));
        response.enqueue(new Callback<List<Pupil>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<Pupil>> call, Response<List<Pupil>> response) {
                try {
                    for (Pupil elem : response.body()) {
                        items.add(elem);
                        Log.d("Response", elem.id + " " + elem.name);
                    }
                    doBindService();
                    createList();
                } catch (Exception e) {
                    Log.d("Error:", "Try again");
                }

            }

            @Override
            public void onFailure(Call<List<Pupil>> call, Throwable t) {
                Log.e("Error", "Could not retrieve data//" + t);
            }
        });

        Call<User> responseProfile = authService.profileUser(authUserResponse);
        System.out.println("ID" + authUserResponse.id + " " + authUserResponse.role);
        responseProfile.enqueue(new Callback<User>() {
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


    public void createList() {
        listPupilAdapter = new ListPupilAdapter(this, items);
        ListView lvMain = (ListView) findViewById(R.id.list_pupil);
        lvMain.setAdapter(listPupilAdapter);
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
        ((TextView) findViewById(R.id.list_pupil_id)).setText("ID:" + String.valueOf(user.id));
        ((TextView) findViewById(R.id.list_pupil_name)).setText(user.name);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.menu_item_exit:
                stopService(intentService);
               // doUnbindService();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    void doBindService() {
        // Attempts to establish a connection with the service.  We use an
        // explicit class name because we want a specific service
        // implementation that we know will be running in our own process
        // (and thus won't be supporting component replacement by other
        // applications).
        intentService = new Intent(ListPupilActivity.this, LocalService.class);
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (Pupil elem : items) {
            ids.add(elem.id);
        }
        intentService.putExtra("ids", ids);
        startForegroundService(intentService);
//        if (bindService(intent, mConnection, Context.BIND_AUTO_CREATE)) {
//            mShouldUnbind = true;
//        } else {
//            Log.e("MY_APP_TAG", "Error: The requested service doesn't " +
//                    "exist, or this client isn't allowed access to it.");
//        }
    }

    void doUnbindService() {
        if (mShouldUnbind) {
            // Release information about the service's state.
            unbindService(mConnection);
            mShouldUnbind = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
     //   doUnbindService();
    }
}