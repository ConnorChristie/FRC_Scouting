package me.connor.frcscouting.listadapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import me.connor.frcscouting.adapters.FilterArrayAdapter;
import me.connor.frcscouting.tabs.matches.items.MatchTeamItem;
import me.connor.frcscouting.tabs.teams.items.TeamItem;

public class ListAdapter extends FilterArrayAdapter<ListItem>
{
	private ListItem listItem;
	private LayoutInflater li;

    private String query = "";
    private boolean isFiltered = false;

    public ListAdapter(Context context, int resource, List<ListItem> items)
	{
		super(context, resource, items);

		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

    public void setFiltered(boolean filtered, String query)
    {
        if (isFiltered = filtered)
        {
            getFilter().filter(this.query = query);
        } else
        {
            isFiltered = false;
            this.query = "";

            notifyDataSetChanged();
        }
    }

	public void updateItems(List<ListItem> items)
	{
        setObjects(items);

        if (isFiltered) getFilter().filter(query);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		listItem = getItem(position);

		return listItem.populate(convertView, li);
	}

	@Override
	public boolean isEnabled(int position)
	{
		return !getItem(position).isHeader();
	}
}