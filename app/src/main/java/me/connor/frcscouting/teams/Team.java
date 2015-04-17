package me.connor.frcscouting.teams;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.connor.frcscouting.matches.TeamStatus;
import me.connor.frcscouting.teaminfo.CategoryItem;

public class Team implements Parcelable
{
	private int id;
	private int teamNumber;

	private String teamName;
	private String comments;

	private List<CategoryItem> categories = new ArrayList<>();

	public Team(int id, int teamNumber, String teamName, String comments)
	{
		this.id = id;
		this.teamNumber = teamNumber;

		this.teamName = teamName;
		this.comments = comments;
	}

	public int getId()
	{
		return id;
	}

	public int getTeamNumber()
	{
		return teamNumber;
	}

	public String getTeamName()
	{
		return teamName;
	}

	public String getComments()
	{
		return comments;
	}

	public List<CategoryItem> getCategories()
	{
		return categories;
	}

	public CategoryItem getCategory(String catName)
	{
		for (CategoryItem category : getCategories())
		{
			if (category.getCategory().equalsIgnoreCase(catName))
			{
				return category;
			}
		}

		return null;
	}

	public TeamStatus getOffenseStatus()
	{
		CategoryItem category = getCategory("Offense");

		if (category != null)
		{
			return TeamStatus.getFromInt(category.getScore());
		}

		return TeamStatus.UNDEFINED;
	}

	public TeamStatus getDefenseStatus()
	{
		CategoryItem category = getCategory("Defense");

		if (category != null)
		{
			return TeamStatus.getFromInt(category.getScore());
		}

		return TeamStatus.UNDEFINED;
	}

	public int getOffenseScore()
	{
		CategoryItem category = getCategory("Offense");

		if (category != null)
		{
			return category.getScore();
		}

		return Integer.MAX_VALUE;
	}

	public int getDefenseScore()
	{
		CategoryItem category = getCategory("Defense");

		if (category != null)
		{
			return category.getScore();
		}

		return Integer.MAX_VALUE;
	}

	public void addCategory(CategoryItem category)
	{
		categories.add(category);
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
		dest.writeInt(teamNumber);

		dest.writeString(teamName);
		dest.writeString(comments);

		dest.writeTypedArray(categories.toArray(new CategoryItem[categories.size()]), flags);
	}

	public static final Creator<Team> CREATOR = new Creator<Team>()
	{
		@Override
		public Team createFromParcel(Parcel source)
		{
			return new Team(source);
		}

		@Override
		public Team[] newArray(int size)
		{
			return new Team[size];
		}
	};

	private Team(Parcel source)
	{
		id = source.readInt();
		teamNumber = source.readInt();

		teamName = source.readString();
		comments = source.readString();

		List<CategoryItem> cats = new ArrayList<>();
		source.readTypedList(cats, CategoryItem.CREATOR);

		categories = cats;
	}
}
