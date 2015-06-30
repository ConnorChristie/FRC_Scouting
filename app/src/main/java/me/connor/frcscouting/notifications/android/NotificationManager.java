package me.connor.frcscouting.notifications.android;

import android.content.Intent;
import android.util.Log;

import me.connor.frcscouting.MainActivity;
import me.connor.frcscouting.notifications.gcm.RegistrationIntentService;

public class NotificationManager
{
    public NotificationManager()
    {
        Log.d("", "Creating the Notification Manager");

        Intent intent = new Intent(MainActivity.getInstance(), RegistrationIntentService.class);
        MainActivity.getInstance().startService(intent);
    }
}
