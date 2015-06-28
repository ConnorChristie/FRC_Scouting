package me.connor.frcscouting.listadapter;

import android.view.LayoutInflater;
import android.view.View;

public abstract class ListItem
{
	public int layout;

	public ListItem(int layout)
	{
		this.layout = layout;
	}

	public View populate(View view, LayoutInflater li)
	{
		if (view == null)
		{
			view = li.inflate(layout, null);
		}

		return view;
	}

	public int getLayout()
	{
		return layout;
	}

    public abstract boolean isHeader();
    public abstract String toString();
}
