package me.connor.frcscouting.tabs.teams.info.tabs.info.items;

import android.view.LayoutInflater;
import android.view.View;

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListItem;

public class CategoryAddItem extends ListItem
{
    private View view;

	public CategoryAddItem()
	{
		super(R.layout.team_info_add_category_item);
	}

    @Override
    public View populate(View v, LayoutInflater li)
    {
        if (view == null)
        {
            view = li.inflate(layout, null);
        }

        return view;
    }

    @Override
    public String toString()
    {
        return "Add Category";
    }

    @Override
	public boolean isHeader()
	{
		return false;
	}
}
