package me.connor.frcscouting.teaminfo;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListItem;

public class CategoryItem extends ListItem implements Parcelable
{
	private int id;
	private int teamId;

	private String category;
	private int score;

	public CategoryItem(int id, int teamId, String category, int score)
	{
		super(R.layout.team_info_list_item);

		this.id = id;
		this.teamId = teamId;

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

	public int getId()
	{
		return id;
	}

	public int getTeamId()
	{
		return teamId;
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

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(id);
		dest.writeInt(teamId);

		dest.writeString(category);
		dest.writeInt(score);
	}

	public static final Creator<CategoryItem> CREATOR = new Creator<CategoryItem>()
	{
		@Override
		public CategoryItem createFromParcel(Parcel source)
		{
			return new CategoryItem(source);
		}

		@Override
		public CategoryItem[] newArray(int size)
		{
			return new CategoryItem[size];
		}
	};

	private CategoryItem(Parcel source)
	{
		super(R.layout.team_info_list_item);

		id = source.readInt();
		teamId = source.readInt();

		category = source.readString();
		score = source.readInt();
	}
}
