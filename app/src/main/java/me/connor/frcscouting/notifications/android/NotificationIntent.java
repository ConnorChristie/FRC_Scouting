package me.connor.frcscouting.notifications.android;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;

import me.connor.frcscouting.notifications.CommonConstants;

public class NotificationIntent extends IntentService
{
    public NotificationIntent()
    {
        super("me.connor.frcscouting.notifications.android");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String action = intent.getAction();

        if (action.equals(CommonConstants.ACTION_DISMISS))
        {
            nm.cancel(CommonConstants.NOTIFICATION_ID);
        }
    }
}
