package me.connor.frcscouting.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.MatchesAdapter;
import me.connor.frcscouting.matches.MItem;
import me.connor.frcscouting.matches.MatchesHeaderItem;
import me.connor.frcscouting.matches.MatchesItem;

public class MatchesFragment extends Fragment
{
	private ListView matchesList;

	private List<MItem> itemsSections = new ArrayList<>();

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

		itemsSections.add(new MatchesHeaderItem(new GregorianCalendar(2015, 4, 11, 16, 45, 19).getTime()));
		itemsSections.add(new MatchesItem(4095, true));
		itemsSections.add(new MatchesItem(2045, true));
		itemsSections.add(new MatchesItem(3584, true));

		itemsSections.add(new MatchesItem(1897, true));
		itemsSections.add(new MatchesItem(3648, true));
		itemsSections.add(new MatchesItem(2497, true));

		matchesList = (ListView) view.findViewById(R.id.matchesList);
		matchesList.setAdapter(new MatchesAdapter(view.getContext(), R.layout.list_item, itemsSections));

		return view;
	}
}
