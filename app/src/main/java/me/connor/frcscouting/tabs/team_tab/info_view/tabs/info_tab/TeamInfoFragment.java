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

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListItem;
import me.connor.frcscouting.tabs.team_tab.info_view.TeamInfoActivity;
import me.connor.frcscouting.tabs.team_tab.info_view.tabs.info_tab.table_items.CategoryAddItem;

public class TeamInfoFragment extends Fragment
{
	public TeamInfoFragment() { }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.fragment_team_info, container, false);
		final TeamInfoActivity teamInfo = (TeamInfoActivity) getActivity();

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

		//teamInfo.getItemsSections().addAll(teamInfo.getTeam().getCategories());
		//teamInfo.getItemsSections().add(new CategoryAddItem());

		HeaderListView statsList = (HeaderListView) view.findViewById(R.id.statsList);
		//statsList.setAdapter(new ListAdapter(teamInfo, R.layout.team_info_list_item, teamInfo.getItemsSections()));

		/*
		statsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{

			}
		});
		*/

		final List<ListItem> stats = new ArrayList<>();

		stats.addAll(teamInfo.getTeam().getCategories());
		stats.add(new CategoryAddItem());

		statsList.setAdapter(new SectionAdapter()
		{
			@Override
			public int numberOfSections()
			{
				return 1;
			}

			@Override
			public int numberOfRows(int section)
			{
				return stats.size();
			}

			@Override
			public View getRowView(int section, int row, View convertView, ViewGroup parent)
			{
				return stats.get(row).populate(convertView, (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
			}

			@Override
			public Object getRowItem(int section, int row)
			{
				return null;
			}

			@Override
			public void onRowItemClick(AdapterView<?> parent, View v, int section, int row, long id)
			{
				if (row == numberOfRows(0) - 1)
				{
					LayoutInflater li = LayoutInflater.from(view.getContext());
					View promptView = li.inflate(R.layout.add_category_prompt, null);

					AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
					dialogBuilder.setView(promptView);

					final EditText categoryName = (EditText) promptView.findViewById(R.id.categoryName);
					final EditText categoryScore = (EditText) promptView.findViewById(R.id.categoryScore);

					dialogBuilder.setCancelable(true)
							.setPositiveButton("Add", new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int id)
								{
									// get user input and set it to result
									// edit text
									//result.setText(userInput.getText());

									Log.d("", categoryName.getText() + ": " + Integer.parseInt(categoryScore.getText().toString()));
								}
							})
							.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int id)
								{
									dialog.cancel();
								}
							});

					AlertDialog alertDialog = dialogBuilder.create();

					alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
					{
						@Override
						public void onCancel(DialogInterface dialog)
						{
							categoryName.clearFocus();
						}
					});

					alertDialog.show();
				}
			}

			@Override
			public boolean onRowItemLongClick(AdapterView<?> parent, View view, int section, int row, long id)
			{
				return false;
			}
		});

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
