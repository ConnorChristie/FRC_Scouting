package me.connor.frcscouting.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.connor.frcscouting.MainActivity;
import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListAdapter;
import me.connor.frcscouting.listadapter.ListItem;
import me.connor.frcscouting.matches.MatchTeamItem;
import me.connor.frcscouting.teaminfo.TeamInfoActivity;
import me.connor.frcscouting.teams.Team;
import me.connor.frcscouting.teams.TeamItem;

public class TeamsFragment extends Fragment
{
	private ListView teamsList;
	private List<ListItem> teams = new ArrayList<>();

	public TeamsFragment() { }

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_teams, container, false);

		for (Team team : ((MainActivity) getActivity()).getTeams())
		{
			teams.add(new TeamItem(team));
		}

		//itemsSections.add(new TeamItem(new Team(4095, "Team RoXI", "")));
		//itemsSections.add(new TeamItem(new Team(3648, "Marquette Warriors", "")));
		//itemsSections.add(new TeamItem(new Team(1642, "King Fishers", "")));

		teamsList = (ListView) view.findViewById(R.id.teamsList);
		teamsList.setAdapter(new ListAdapter(view.getContext(), R.layout.team_list_item, teams));

		teamsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				TeamItem teamItem = (TeamItem) parent.getItemAtPosition(position);
				Intent teamInfoIntent = new Intent(getActivity(), TeamInfoActivity.class);

				teamInfoIntent.putExtra("teamData", teamItem.getTeam());

				startActivity(teamInfoIntent);
			}
		});

		teamsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
			{
				Object clicked = parent.getItemAtPosition(position);

				if (clicked instanceof MatchTeamItem)
				{
					MatchTeamItem item = (MatchTeamItem) clicked;

					if (item.getSide().equals("OFFENSE"))
						item.setSide("DEFENSE");
					else
						item.setSide("OFFENSE");

					teamsList.invalidateViews();

					Toast.makeText(view.getContext(), "Changed side to " + item.getSide(), Toast.LENGTH_LONG).show();
				}

				return true;
			}
		});

		return view;
	}

	public String toString()
	{
		return "Teams";
	}
}
