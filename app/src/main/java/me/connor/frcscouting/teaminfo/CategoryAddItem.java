package me.connor.frcscouting.teaminfo;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListItem;

public class CategoryAddItem extends ListItem
{
	public CategoryAddItem()
	{
		super(R.layout.team_info_add_category_item);
	}

	@Override
	public boolean isHeader()
	{
		return false;
	}
}
