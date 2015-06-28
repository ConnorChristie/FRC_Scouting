package me.connor.frcscouting.notifications.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import me.connor.frcscouting.MainActivity;
import me.connor.frcscouting.notifications.CommonConstants;
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
