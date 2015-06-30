package me.connor.frcscouting.tabs.teams.info.tabs.info.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;

import com.applidium.headerlistview.HeaderListView;
import com.applidium.headerlistview.SectionAdapter;

import java.util.ArrayList;
import java.util.List;

import me.connor.frcscouting.MainActivity;
import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListItem;
import me.connor.frcscouting.tabs.teams.Team;
import me.connor.frcscouting.tabs.teams.info.tabs.info.items.CategoryItem;
import me.connor.frcscouting.tabs.teams.info.tabs.info.items.CategoryListItem;

public class CategoryListAdapter extends SectionAdapter
{
    private MainActivity activity;

	private Team team;
	private View view;

	private List<ListItem> stats;
	private HeaderListView statsList;

	public CategoryListAdapter(HeaderListView statsList, List<ListItem> stats, View view, Team team)
	{
        super(statsList);

		this.statsList = statsList;
		this.stats = stats;

		this.view = view;
		this.team = team;

        activity = MainActivity.getInstance();
	}

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

	public List<ListItem> getUpdatedStats()
	{
		for (ListItem item : stats)
		{
			if (item instanceof CategoryItem)
			{
				((CategoryItem) item).setEditedScore();
			}
		}

		return stats;
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
			View promptView = li.inflate(R.layout.dialog_add_category, null);

			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
			dialogBuilder.setView(promptView);

			final EditText categoryName = (EditText) promptView.findViewById(R.id.categoryName);
			final EditText categoryScore = (EditText) promptView.findViewById(R.id.categoryScore);

			dialogBuilder.setTitle("Add Category").setCancelable(true).setPositiveButton("Add", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					List<ListItem> newStats = new ArrayList<>();

					int catId = activity.getDatabase().add(new CategoryListItem(categoryName.getText().toString(), Integer.parseInt(categoryScore.getText().toString()))).getId();
					CategoryItem catItem = activity.getDatabase().add(new CategoryItem(0, team.getId(), catId, categoryName.getText().toString(), Integer.parseInt(categoryScore.getText().toString())));

					newStats.addAll(stats);
					newStats.add(stats.size() - 1, catItem);

					CategoryListAdapter newAdapter = new CategoryListAdapter(statsList, newStats, view, team);

					statsList.setAdapter(newAdapter);
					newAdapter.notifyDataSetChanged();

					//Get all other teams and add category

					List<Team> teams = activity.getTeams();

					for (Team t : teams)
					{
						CategoryItem cat = t.getCategory(categoryName.getText().toString());

						if (team.getId() != t.getId() && cat == null)
						{
							catItem = activity.getDatabase().add(new CategoryItem(0, t.getId(), catId, categoryName.getText().toString(), Integer.parseInt(categoryScore.getText().toString())));

							t.addCategory(catItem);
						}
					}
				}
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
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
					categoryScore.clearFocus();
				}
			});

			alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			alertDialog.show();
		}
	}

	@Override
	public boolean onRowItemLongClick(AdapterView<?> parent, View view, int section, int row, long id)
	{
		return false;
	}

	public void deleteCategory(int id)
	{
		List<ListItem> newStats = new ArrayList<>();

        activity.getDatabase().deleteCategory(id);

		for (Team t : activity.getTeams())
		{
			t.deleteCategory(id);
		}

		for (ListItem i : stats)
		{
			if (i instanceof CategoryItem)
			{
				CategoryItem cat = (CategoryItem) i;

				if (cat.getCategoryId() == id) continue;
			}

			newStats.add(i);
		}

		CategoryListAdapter newAdapter = new CategoryListAdapter(statsList, newStats, view, team);

		statsList.setAdapter(newAdapter);
		newAdapter.notifyDataSetChanged();
	}

	public List<ListItem> getStats()
	{
		return stats;
	}
}