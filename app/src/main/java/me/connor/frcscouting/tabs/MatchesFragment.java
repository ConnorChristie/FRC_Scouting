package me.connor.frcscouting.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applidium.headerlistview.HeaderListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.connor.frcscouting.R;
import me.connor.frcscouting.adapters.FilterSectionAdapter;
import me.connor.frcscouting.tabs.matches.MatchB;
import me.connor.frcscouting.tabs.teams.Team;

public class MatchesFragment extends Fragment
{
    private HeaderListView matchesList;
    private List<MatchB> matches = new ArrayList<>();

    public MatchesFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    //TODO: Add event_key sorting

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_matches, container, false);

        matchesList = (HeaderListView) view.findViewById(R.id.matchesList);
        matchesList.setAdapter(new FilterSectionAdapter(view, this, matchesList, matches));

        return view;
    }

    public void updateMatches(List<MatchB> matchEntries)
    {
        matches.clear();
        matches.addAll(matchEntries);

        matchesList.getAdapter().notifyDataSetChanged();
    }

    public void setMatches(List<MatchB> matches)
    {
        this.matches = matches;

        Collections.sort(matches);
    }

    public void updateMatches(Team team)
    {
        for (MatchB match : matches)
        {
            match.setTeam(team);
        }

        matchesList.getAdapter().notifyDataSetChanged();
    }

    public HeaderListView getMatchesList()
    {
        return matchesList;
    }

    public MatchB getMatchFromKey(String key)
    {
        for (MatchB match : matches)
        {
            if (match.getMatchKey().equalsIgnoreCase(key))
            {
                return match;
            }
        }

        return null;
    }

    public String toString()
    {
        return "Matches";
    }
}
