package me.connor.frcscouting.thebluealliance.api;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.connor.frcscouting.MainActivity;
import me.connor.frcscouting.tabs.matches.attributes.Alliance;
import me.connor.frcscouting.tabs.matches.Match;
import me.connor.frcscouting.tabs.matches.attributes.Side;
import me.connor.frcscouting.tabs.matches.items.MatchHeaderItem;
import me.connor.frcscouting.tabs.matches.items.MatchTeamItem;
import me.connor.frcscouting.tabs.teams.Team;
import me.connor.frcscouting.thebluealliance.Links;

public class MatchesAPI extends AsyncTask<Void, Match, Integer>
{
    private MainActivity activity;

    private String eventKey;
    private int team;

    private List<Match> matches = new ArrayList<>();

    public MatchesAPI(MainActivity activity, String eventKey, int team)
    {
        this.activity = activity;

        this.eventKey = eventKey;
        this.team = team;
    }

    @Override
    protected Integer doInBackground(Void... params)
    {
        try
        {
            HttpURLConnection con = (HttpURLConnection) new URL(String.format(Links.MATCHES_API, eventKey)).openConnection();
            con.setRequestProperty("X-TBA-App-Id", "frc4095:scouting-system:1.0");

            int resp = con.getResponseCode();

            if (resp == HttpURLConnection.HTTP_OK)
            {
                JSONArray json = new JSONArray(IOUtils.toString(con.getInputStream()));

                for (int i = 0; i < json.length(); i++)
                {
                    JSONObject matchObj = json.getJSONObject(i);
                    JSONObject alliances = matchObj.getJSONObject("alliances");

                    JSONArray redTeamsArr = alliances.getJSONObject("red").getJSONArray("teams");
                    JSONArray blueTeamsArr = alliances.getJSONObject("blue").getJSONArray("teams");

                    Integer[] redTeams = getTeamNumbers(redTeamsArr);
                    Integer[] blueTeams = getTeamNumbers(blueTeamsArr);

                    boolean isRedTeam = Arrays.asList(redTeams).contains(team);
                    boolean isBlueTeam = Arrays.asList(blueTeams).contains(team);

                    if (isRedTeam || isBlueTeam)
                    {
                        String matchTitle = matchObj.getString("comp_level");

                        if (matchTitle.equals("f"))
                            matchTitle = "Final";
                        else if (matchTitle.equals("sf"))
                            matchTitle = "Semifinal";
                        else if (matchTitle.equals("qf"))
                            matchTitle = "Quarterfinal";
                        else if (matchTitle.equals("qm"))
                            matchTitle = "Qualification";

                        String matchTime = new SimpleDateFormat("MMM dd 'at' h:mm a").format(new Date(matchObj.getLong("time") * 1000));

                        Map<Integer, MatchTeamItem> teams = new LinkedHashMap<>();

                        for (int teamNumber : redTeams)
                        {
                            if (activity.hasTeam(teamNumber))
                            {
                                teams.put(teamNumber, new MatchTeamItem(activity.getTeam(teamNumber), Alliance.RED, Side.OFFENSE));
                            } else
                            {
                                teams.put(teamNumber, new MatchTeamItem(new Team(1, teamNumber, "Team " + teamNumber, ""), Alliance.RED, Side.OFFENSE));
                            }
                        }

                        for (int teamNumber : blueTeams)
                        {
                            if (activity.hasTeam(teamNumber))
                            {
                                teams.put(teamNumber, new MatchTeamItem(activity.getTeam(teamNumber), Alliance.BLUE, Side.OFFENSE));
                            } else
                            {
                                teams.put(teamNumber, new MatchTeamItem(new Team(1, teamNumber, "Team " + teamNumber, ""), Alliance.BLUE, Side.OFFENSE));
                            }
                        }

                        publishProgress(activity.getDatabase().add(new Match(1, matchObj.getString("key"), new MatchHeaderItem(matchTitle, matchObj.getInt("match_number"), matchTime), teams)));
                    }
                }
            } else
            {
                return resp;
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    private Integer[] getTeamNumbers(JSONArray arr)
    {
        Integer[] teams = new Integer[3];

        for (int i = 0; i < arr.length(); i++)
        {
            try
            {
                teams[i] = Integer.parseInt(arr.getString(i).replace("frc", ""));
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return teams;
    }

    @Override
    protected void onProgressUpdate(Match... progress)
    {
        matches.add(progress[0]);
        Collections.sort(matches);

        MainActivity.getInstance().getPagerAdapter().getMatchesFragment().updateMatches(matches);
    }

    @Override
    protected void onPostExecute(Integer result)
    {
        if (result != HttpURLConnection.HTTP_OK)
        {
            new TeamsAPI(matches).execute();

            Toast.makeText(activity, "Downloading team data...", Toast.LENGTH_LONG).show();
        } else
        {
            final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

            alertDialog.setTitle("No Matches");
            alertDialog.setMessage("No matches could be found for that event. Are you sure that event still exists?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        }
    }
}
