package me.connor.frcscouting.events;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import me.connor.frcscouting.MainActivity;
import me.connor.frcscouting.thebluealliance.Links;

public class EventsFetcher extends AsyncTask<Void, Event, Void>
{
    @Override
    protected Void doInBackground(Void... params)
    {
        try
        {
            for (CSVRecord record : CSVFormat.EXCEL.withHeader().parse(new BufferedReader(new InputStreamReader(new URL(Links.eventsCsv).openStream()))))
            {
                publishProgress(new Event(record.get("event_name"), record.get("event_code")));
            }
        } catch (Exception e)
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