package me.connor.frcscouting.thebluealliance.api;

import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import me.connor.frcscouting.MainActivity;
import me.connor.frcscouting.tabs.matches.MatchB;
import me.connor.frcscouting.tabs.matches.items.MatchTeamItem;
import me.connor.frcscouting.tabs.teams.Team;
import me.connor.frcscouting.thebluealliance.Links;

public class TeamsAPI extends AsyncTask<Void, Team, Void>
{
    private MainActivity activity;
    private List<MatchB> matches;

    public TeamsAPI(List<MatchB> matches)
    {
        this.matches = matches;
        activity = MainActivity.getInstance();
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        //Load all teams that were populated by the matches...

        for (MatchB match : matches)
        {
            for (MatchTeamItem teamItem : match.getMatchTeams())
            {
                Team team = teamItem.getTeam();

                if (!activity.hasTeam(team.getTeamNumber()))
                {
                    try
                    {
                        HttpURLConnection con = (HttpURLConnection) new URL(String.format(Links.TEAMS_API, "frc" + team.getTeamNumber())).openConnection();
                        con.setRequestProperty("X-TBA-App-Id", "frc4095:scouting-system:1.0");

                        if (con.getResponseCode() == 404)
                        {
                            Toast.makeText(MainActivity.getInstance(), "Could not get data for team: " + team.getTeamNumber(), Toast.LENGTH_LONG).show();
                        } else
                        {
                            JSONObject teamObj = new JSONObject(IOUtils.toString(con.getInputStream()));

                            team.setTeamName(teamObj.getString("nickname"));
                        }

                        con.disconnect();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    activity.getDatabase().addNewTeam(team, true);

                    publishProgress(team);
                } else
                {
                    Team newTeam = activity.getTeam(team.getTeamNumber());

                    teamItem.setTeam(newTeam);
                    publishProgress(newTeam);
                }
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Team... progress)
    {
        //Update teams list and matches list

        activity.getPagerAdapter().getMatchesFragment().updateMatches(matches);
        activity.getPagerAdapter().getTeamsFragment().updateTeam(progress[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        Toast.makeText(activity, "Done gathering team data.", Toast.LENGTH_SHORT).show();
    }
}
