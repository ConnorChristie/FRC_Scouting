package me.connor.frcscouting.listadapter;

import android.view.LayoutInflater;
import android.view.View;

public abstract class ListItem
{
	protected int layout;

	public abstract boolean isHeader();

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
}
