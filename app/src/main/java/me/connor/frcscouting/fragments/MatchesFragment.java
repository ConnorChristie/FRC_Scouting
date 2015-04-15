package me.connor.frcscouting.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListAdapter;
import me.connor.frcscouting.listadapter.ListItem;
import me.connor.frcscouting.matches.MatchHeaderItem;
import me.connor.frcscouting.matches.MatchItem;
import me.connor.frcscouting.matches.TeamStatus;

public class MatchesFragment extends Fragment
{
	private ListView matchesList;
	private List<ListItem> itemsSections = new ArrayList<>();

	public MatchesFragment() { }

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_matches, container, false);

		itemsSections.add(new MatchHeaderItem(new GregorianCalendar(2015, 4, 11, 16, 45, 19).getTime()));
		itemsSections.add(new MatchItem(4095, "Team RoXI", true, TeamStatus.BAD, "OFFENSE"));
		itemsSections.add(new MatchItem(2045, "Robonauts", true, TeamStatus.GOOD, "OFFENSE"));
		itemsSections.add(new MatchItem(3584, "Gladiators", true, TeamStatus.GOOD, "DEFENSE"));

		itemsSections.add(new MatchItem(1897, "King Fishers", false, TeamStatus.AVERAGE, "DEFENSE"));
		itemsSections.add(new MatchItem(3648, "Marquette Warriors", false, TeamStatus.BAD, "OFFENSE"));
		itemsSections.add(new MatchItem(2497, "Dolphins", false, TeamStatus.AVERAGE, "DEFENSE"));

		matchesList = (ListView) view.findViewById(R.id.matchesList);
		matchesList.setAdapter(new ListAdapter(view.getContext(), R.layout.list_item, itemsSections));

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

				if (clicked instanceof MatchItem)
				{
					MatchItem item = (MatchItem) clicked;

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

		return view;
	}

	public String toString()
	{
		return "Matches";
	}
}
