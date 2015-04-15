package me.connor.frcscouting.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class ListAdapter extends ArrayAdapter<ListItem>
{
	private ListItem listItem;
	private LayoutInflater li;

	public ListAdapter(Context context, int resource, List<ListItem> items)
	{
		super(context, resource, items);

		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
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