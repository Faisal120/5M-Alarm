package com.example.a5malarm;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.a5malarm.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    private static final int BROADCAST_ID = 0;
    ArrayList<PendingIntent> intentArrayList = new ArrayList<>();
    int alrmtim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "AlarmChannel";
            String desc = "Tap to disable alarm";
            int priority = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("channel_key", channelName, priority);
            notificationChannel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    {
                                        Calendar crnTime = Calendar.getInstance();
                                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss a");
                                        binding.currentTime.setText(formatter.format(crnTime.getTime()));
                                    }
                                }
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        int size = 1;
        final int[] i = {size};
        int n = 1;
        binding.setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlarmReciever.class);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, i[0], intent, 0);
                int calTime = alrmtim + 5 * 1000 * 60;
                long alarmTime = System.currentTimeMillis() + calTime;
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime + i[0], pendingIntent);
                alrmtim = calTime + 5;
                i[0]++;

                Toast.makeText(MainActivity.this, "Alarm set!", Toast.LENGTH_SHORT).show();
                    intentArrayList.add(pendingIntent);
            }
        });
    }
}