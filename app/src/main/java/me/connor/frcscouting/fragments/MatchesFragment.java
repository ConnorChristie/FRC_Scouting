package me.connor.frcscouting.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.applidium.headerlistview.HeaderListView;
import com.applidium.headerlistview.SectionAdapter;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import me.connor.frcscouting.MainActivity;
import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListAdapter;
import me.connor.frcscouting.listadapter.ListItem;
import me.connor.frcscouting.matches.Match;
import me.connor.frcscouting.matches.MatchHeaderItem;
import me.connor.frcscouting.matches.MatchTeamItem;

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

		matches.add(new Match(new MatchHeaderItem(new GregorianCalendar(2015, 4, 11, 16, 45, 0).getTime()), new MatchTeamItem[] {
				new MatchTeamItem(((MainActivity) getActivity()).getTeam(1), true, "OFFENSE"),
				new MatchTeamItem(((MainActivity) getActivity()).getTeam(3), true, "OFFENSE"),
				new MatchTeamItem(((MainActivity) getActivity()).getTeam(2), true, "DEFENSE"),

				new MatchTeamItem(((MainActivity) getActivity()).getTeam(2), false, "DEFENSE"),
				new MatchTeamItem(((MainActivity) getActivity()).getTeam(1), false, "OFFENSE"),
				new MatchTeamItem(((MainActivity) getActivity()).getTeam(3), false, "DEFENSE"),
		}));

		matches.add(new Match(new MatchHeaderItem(new GregorianCalendar(2015, 4, 11, 17, 30, 0).getTime()), new MatchTeamItem[] {
				new MatchTeamItem(((MainActivity) getActivity()).getTeam(1), true, "OFFENSE"),
				new MatchTeamItem(((MainActivity) getActivity()).getTeam(3), true, "OFFENSE"),
				new MatchTeamItem(((MainActivity) getActivity()).getTeam(2), true, "DEFENSE"),

				new MatchTeamItem(((MainActivity) getActivity()).getTeam(2), false, "DEFENSE"),
				new MatchTeamItem(((MainActivity) getActivity()).getTeam(1), false, "OFFENSE"),
				new MatchTeamItem(((MainActivity) getActivity()).getTeam(3), false, "DEFENSE"),
		}));

		matchesList = (HeaderListView) view.findViewById(R.id.matchesList);
		//matchesList.setAdapter(new ListAdapter(view.getContext(), R.layout.match_list_item, matches));

		matchesList.setAdapter(new SectionAdapter()
		{
			@Override
			public int numberOfSections()
			{
				return matches.size();
			}

			@Override
			public int numberOfRows(int section)
			{
				return matches.get(section).getMatchTeams().size();
			}

			@Override
			public View getRowView(int section, int row, View convertView, ViewGroup parent)
			{
				MatchTeamItem item = (MatchTeamItem) getRowItem(section, row);

				return item.populate(convertView, (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
			}

			@Override
			public View getSectionHeaderView(int section, View convertView, ViewGroup parent)
			{
				MatchHeaderItem item = matches.get(section).getHeader();

				return item.populate(convertView, (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
			}

			@Override
			public boolean hasSectionHeaderView(int section) {
				return true;
			}

			@Override
			public Object getRowItem(int section, int row)
			{
				Match match = matches.get(section);

				return match.getMatchTeams().get(row);
			}

			@Override
			public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id)
			{

			}

			@Override
			public boolean onRowItemLongClick(AdapterView<?> parent, View view, int section, int row, long id)
			{
				if (isSectionHeader((int) id)) return false;

				MatchTeamItem item = (MatchTeamItem) getRowItem(section, row);

				if (item.getSide().equals("OFFENSE"))
					item.setSide("DEFENSE");
				else
					item.setSide("OFFENSE");

				notifyDataSetChanged();

				Toast.makeText(view.getContext(), "Changed side to " + item.getSide(), Toast.LENGTH_LONG).show();

				return true;
			}
		});

		/*
		matchesList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				System.out.println("Hello Clicked");
			}
		});

		matchesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
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

					matchesList.invalidateViews();

					Toast.makeText(view.getContext(), "Changed side to " + item.getSide(), Toast.LENGTH_LONG).show();
				}

				return true;
			}
		});
		*/

		return view;
	}

	public String toString()
	{
		return "Matches";
	}
}
