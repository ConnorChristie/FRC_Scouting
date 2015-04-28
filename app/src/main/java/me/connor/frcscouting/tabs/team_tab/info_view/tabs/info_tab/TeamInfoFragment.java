package me.connor.frcscouting.tabs.team_tab.info_view.tabs.info_tab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.applidium.headerlistview.HeaderListView;
import com.applidium.headerlistview.SectionAdapter;

import java.util.ArrayList;
import java.util.List;

import me.connor.frcscouting.MainActivity;
import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListItem;
import me.connor.frcscouting.tabs.team_tab.info_view.TeamInfoActivity;
import me.connor.frcscouting.tabs.team_tab.info_view.tabs.info_tab.adapter.CategoryListAdapter;
import me.connor.frcscouting.tabs.team_tab.info_view.tabs.info_tab.table_items.CategoryAddItem;
import me.connor.frcscouting.tabs.team_tab.info_view.tabs.info_tab.table_items.CategoryItem;

public class TeamInfoFragment extends Fragment
{
	private static HeaderListView statsList;

	public EditText teamName;
	public EditText teamNumber;

	public TeamInfoFragment() { }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.fragment_team_info, container, false);
		final TeamInfoActivity teamInfo = (TeamInfoActivity) getActivity();

		((TextView) view.findViewById(R.id.team_name)).setText(teamInfo.getTeam().getTeamName());
		((TextView) view.findViewById(R.id.team_number)).setText(teamInfo.getTeam().getTeamNumber() + "");

		teamName = (EditText) view.findViewById(R.id.team_name);
		teamNumber = (EditText) view.findViewById(R.id.team_number);

		teamName.setOnEditorActionListener(teamInfo.textEditDoneEvent);
		teamNumber.setOnEditorActionListener(teamInfo.textEditDoneEvent);

		statsList = (HeaderListView) view.findViewById(R.id.statsList);

		final List<ListItem> stats = new ArrayList<>();

		stats.addAll(teamInfo.getTeam().getCategories());
		stats.add(new CategoryAddItem());

		statsList.setAdapter(new CategoryListAdapter(statsList, stats, view, teamInfo.getTeam()));

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	public String toString()
	{
		return "Info";
	}

	public static HeaderListView getStatsList()
	{
		return statsList;
	}
}
