package com.example.personalityidmobail;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.personalityidmobail.models.FirebaseModel;
import com.example.personalityidmobail.models.Pupil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LocalService extends Service {

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private NotificationManager mNM;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;
    public String ANDROID_CHANNEL_ID = "channel";
    public String ANDROID_CHANNEL_NAME = "channel";
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        // Sets whether notifications posted to this channel should display notification lights
        androidChannel.enableLights(true);
        // Sets whether notification posted to this channel should vibrate.
        androidChannel.enableVibration(true);
        // Sets the notification light color for notifications posted to this channel
        androidChannel.setLightColor(Color.BLUE);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        mNM.createNotificationChannel(androidChannel);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_baseline_message_24)  // the status icon
                .setTicker("text")  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("name")  // the label of the entry
                .setContentText("text")  // the contents of the entry
                .setChannelId("channel")  // The intent to send when the entry is clicked
                .build();
        startForeground(1,
                notification);
        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStart");
        Bundle arguments = intent.getExtras();
        ArrayList<Integer> ids = (ArrayList<Integer>) arguments.getSerializable("ids");
        ArrayList<DatabaseReference> databaseReferences = new ArrayList<DatabaseReference>();
        System.out.println("onBind" + ids.size());
        for (Integer elem : ids) {
            System.out.println(elem + "");
            DatabaseReference myRef = database.getReference("Pupils/" + elem);
            myRef.addChildEventListener(new ChildEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    System.out.println("onChildAdded");
                    FirebaseModel firebaseModel = snapshot.getValue(FirebaseModel.class);
                    showNotification(firebaseModel.Name, firebaseModel.Message, firebaseModel.PupilId,
                            firebaseModel.EventTime);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            databaseReferences.add(myRef);
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.local_service_started);

        // The PendingIntent to launch our activity if the user selects this notification

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_baseline_message_24)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(getText(R.string.local_service_label))  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification(String name, String message, long pupilId, String eventTime) {
        // In this sample, we'll use the same text for the ticker and the expanded notification

        eventTime = parseDateTime(eventTime);
        CharSequence text = eventTime + " " + message;

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_baseline_message_24)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(name)  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent).setChannelId("channel")  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        mNM.notify((int)pupilId, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String parseDateTime(String dateTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);

        DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = localDateTime.format(aFormatter);

        aFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = localDateTime.format(aFormatter);

        return  formattedTime + " " + formattedDate;
    }

}
