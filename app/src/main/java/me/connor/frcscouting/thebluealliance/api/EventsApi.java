package me.connor.frcscouting.thebluealliance.api;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import me.connor.frcscouting.MainActivity;
import me.connor.frcscouting.events.Event;
import me.connor.frcscouting.thebluealliance.Links;

public class EventsApi extends AsyncTask<Void, Event, Void>
{
    //TODO: Cache the list of events.

    @Override
    protected Void doInBackground(Void... voids)
    {
        try
        {
            HttpURLConnection con = (HttpURLConnection) new URL(String.format(Links.EVENTS_API, Calendar.getInstance().get(Calendar.YEAR))).openConnection();
            con.setRequestProperty("X-TBA-App-Id", "frc4095:scouting-system:1.0");

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                JSONArray json = new JSONArray(IOUtils.toString(con.getInputStream()));

                for (int i = 0; i < json.length(); i++)
                {
                    JSONObject eventObj = json.getJSONObject(i);

                    publishProgress(new Event(eventObj.getString("name"), eventObj.getString("key")));
                }
            }
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (JSONException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Event... progress)
    {
        MainActivity.getInstance().getEventList().add(progress[0]);
    }
}
