package me.connor.frcscouting.teamfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListAdapter;
import me.connor.frcscouting.teaminfo.CategoryAddItem;
import me.connor.frcscouting.teaminfo.CategoryItem;
import me.connor.frcscouting.teaminfo.TeamInfo;

public class TeamInfoFragment extends Fragment
{
	public TeamInfoFragment() { }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.fragment_team_info, container, false);
		final TeamInfo teamInfo = (TeamInfo) getActivity();

		((TextView) view.findViewById(R.id.team_name)).setText(teamInfo.getTeam().getTeamName());
		((TextView) view.findViewById(R.id.team_number)).setText(teamInfo.getTeam().getTeamNumber() + "");

		teamInfo.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				teamInfo.finish();
			}
		});

		EditText teamName = (EditText) view.findViewById(R.id.team_name);
		EditText teamNumber = (EditText) view.findViewById(R.id.team_number);

		teamName.setOnEditorActionListener(teamInfo.textEditDoneEvent);
		teamNumber.setOnEditorActionListener(teamInfo.textEditDoneEvent);

		//teamInfo.getItemsSections().add(new CategoryItem(1, 1, "Offense", 8));
		//teamInfo.getItemsSections().add(new CategoryItem(1, 1, "Defense", 4));

		for (CategoryItem cat : teamInfo.getTeam().getCategories())
		{
			System.out.println(cat.getCategory());
		}

		System.out.println("Size: " + teamInfo.getTeam().getCategories().size());

		teamInfo.getItemsSections().addAll(teamInfo.getTeam().getCategories());
		teamInfo.getItemsSections().add(new CategoryAddItem());

		ListView statsList = (ListView) view.findViewById(R.id.statsList);
		statsList.setAdapter(new ListAdapter(teamInfo, R.layout.team_info_list_item, teamInfo.getItemsSections()));

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
}
