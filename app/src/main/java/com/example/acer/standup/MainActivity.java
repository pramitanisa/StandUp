package com.example.acer.standup;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import static com.example.acer.standup.R.id.alarmToggle;

public class MainActivity extends AppCompatActivity {

    private NotificationManager mNotificationManager;

    private static final int NOTIFICATION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        ToggleButton alarmToggle = (ToggleButton) findViewById(R.id.alarmToggle);

        Intent notifyIntent = new Intent(this, AlarmReceiver.class);

        boolean alarmUp = (PendingIntent.getBroadcast(this, 0, notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);

        alarmToggle.setChecked(alarmUp);

        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmToggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                        String toastMessage;

                        if (isChecked) {
                            long triggerTime = SystemClock.elapsedRealtime()
                                    + AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                            long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                            //If the Toggle is turned on, set the repeating alarm with a 15 minute interval
                            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                    triggerTime, repeatInterval, notifyPendingIntent);

                            toastMessage = getString(R.string.alarm_on_toast);
                        } else {
                            alarmManager.cancel(notifyPendingIntent);
                            //Cansel notification if the alarm is turned off
                            mNotificationManager.cancelAll();

                            //Set the toast message for the "off" case
                            toastMessage = getString(R.string.alarm_off_toast);
                        }
                        //Show a toast to say the alarm is turned on or off
                        Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT)
                                .show();

                    }
                });

    }

}