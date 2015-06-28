package me.connor.frcscouting.tabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.applidium.headerlistview.HeaderListView;
import com.applidium.headerlistview.SectionAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.connor.frcscouting.R;
import me.connor.frcscouting.adapters.FilterSectionAdapter;
import me.connor.frcscouting.tabs.matches.Match;
import me.connor.frcscouting.tabs.matches.attributes.Side;
import me.connor.frcscouting.tabs.matches.items.MatchHeaderItem;
import me.connor.frcscouting.tabs.matches.items.MatchTeamItem;
import me.connor.frcscouting.tabs.teams.Team;
import me.connor.frcscouting.tabs.teams.info.TeamInfoActivity;

public class MatchesFragment extends Fragment
{
	private HeaderListView matchesList;
	private List<Match> matches = new ArrayList<>();

	public MatchesFragment() { }

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.fragment_matches, container, false);

        matchesList = (HeaderListView) view.findViewById(R.id.matchesList);
		matchesList.setAdapter(new FilterSectionAdapter(view, this, matchesList, matches));

		return view;
	}

	public void updateMatches(List<Match> matchEntries)
	{
        matches.clear();
		matches.addAll(matchEntries);

        matchesList.getAdapter().notifyDataSetChanged();
	}

    public void setMatches(List<Match> matches)
    {
        this.matches = matches;

        Collections.sort(matches);
    }

    public void updateMatches(Team team)
    {
        for (Match match : matches)
        {
            match.setTeam(team);
        }

        matchesList.getAdapter().notifyDataSetChanged();
    }

    public HeaderListView getMatchesList()
    {
        return matchesList;
    }

    public Match getMatchFromKey(String key)
    {
        for (Match match : matches)
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
