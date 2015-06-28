package me.connor.frcscouting.notifications.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import me.connor.frcscouting.MainActivity;
import me.connor.frcscouting.R;
import me.connor.frcscouting.notifications.CommonConstants;
import me.connor.frcscouting.notifications.android.NotificationIntent;
import me.connor.frcscouting.tabs.matches.Match;

public class GcmListenerService extends com.google.android.gms.gcm.GcmListenerService
{
    @Override
    public void onMessageReceived(String from, Bundle data)
    {
        Log.d("", "Event Key: " + data.getString("event_key"));
        Log.d("", "Saved Key: " + MainActivity.getInstance().getSharedPreferences().getString("event_key", ""));

        switch (data.getString("msg_type").toLowerCase())
        {
            case "match_score":
                Match match = MainActivity.getInstance().getPagerAdapter().getMatchesFragment().getMatchFromKey(data.getString("match_key"));

                if (match != null)
                {
                    Log.d("", "Match Data: " + data.getString("match_data"));

                    try
                    {
                        JSONObject matchData = new JSONObject(data.getString("match_data"));

                        Log.d("", "Blue Score: " + matchData.getJSONObject("alliances").getJSONObject("blue").getInt("score"));
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                break;
            default:
                sendNotification(data);

                break;
        }
    }

    private void sendNotification(Bundle data)
    {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String title = data.getString("title");
        String message = data.getString("message");

        Intent dismissIntent = new Intent(this, NotificationIntent.class);
        dismissIntent.setAction(CommonConstants.ACTION_DISMISS);
        PendingIntent piDismiss = PendingIntent.getService(this, 0, dismissIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_robot)
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));
                //.addAction(R.drawable.ic_stat_dismiss, getString(R.string.dismiss), piDismiss);

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(CommonConstants.MESSAGE, message);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        mNotificationManager.notify(CommonConstants.NOTIFICATION_ID, builder.build());
    }
}
