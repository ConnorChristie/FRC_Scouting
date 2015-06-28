package me.connor.frcscouting.notifications.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

import me.connor.frcscouting.R;
import me.connor.frcscouting.notifications.CommonConstants;

public class RegistrationIntentService extends IntentService
{
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    private SharedPreferences sharedPreferences;

    public RegistrationIntentService()
    {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try
        {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG)
            {
                // [START register_for_gcm]
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.
                // [START get_token]
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                // [END get_token]
                Log.i(TAG, "GCM Registration Token: " + token);

                if (!sharedPreferences.getBoolean(CommonConstants.SENT_TOKEN_TO_SERVER, false))
                {
                    sendRegistrationToServer(token);
                }

                // Subscribe to topic channels
                subscribeTopics(token);

                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.
                sharedPreferences.edit().putBoolean(CommonConstants.SENT_TOKEN_TO_SERVER, true).apply();
            }
        } catch (Exception e)
        {
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(CommonConstants.SENT_TOKEN_TO_SERVER, false).apply();
        }

        LocalBroadcastManager context = LocalBroadcastManager.getInstance(this);

        context.sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
        context.sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));
    }

    private void sendRegistrationToServer(String token) throws IOException, NoSuchAlgorithmException
    {
        //Send the token to the ubuntu server

        HttpURLConnection con = (HttpURLConnection) new URL(CommonConstants.REGISTRATION_URL).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());

        wr.writeBytes("regId=" + token + "&checksum=" + sha1(CommonConstants.API_KEY + token));
        wr.flush();
        wr.close();

        if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            Log.d("", "Response from server: " + IOUtils.toString(con.getInputStream()));
        } else
        {
            Log.d("", "Server did not accept the reg id");
        }
    }

    private void subscribeTopics(String token) throws IOException
    {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        pubSub.subscribe(token, "/topics/global", null);
    }

    private String sha1(String input) throws NoSuchAlgorithmException
    {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < result.length; i++)
        {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}