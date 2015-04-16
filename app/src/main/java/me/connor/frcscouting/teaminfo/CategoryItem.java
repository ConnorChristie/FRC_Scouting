package me.connor.frcscouting.teaminfo;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListItem;

public class CategoryItem extends ListItem
{
	private String category;
	private int score;

	public CategoryItem(String category, int score)
	{
		super(R.layout.team_info_list_item);

		this.category = category;
		this.score = score;
	}

	@Override
	public View populate(View view, LayoutInflater li)
	{
		view = super.populate(view, li);

		TextView category = (TextView) view.findViewById(R.id.category);
		EditText score = (EditText) view.findViewById(R.id.score);

		category.setText(getCategory());
		score.setText(getScore() + "");

		return view;
	}

	public String getCategory()
	{
		return category;
	}

	public int getScore()
	{
		return score;
	}

	@Override
	public boolean isHeader()
	{
		return false;
	}
}
