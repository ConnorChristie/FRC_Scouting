package me.connor.frcscouting.tabs.teams;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import me.connor.frcscouting.database.models.Teams;
import me.connor.frcscouting.interfaces.Column;
import me.connor.frcscouting.interfaces.Item;
import me.connor.frcscouting.tabs.teams.attributes.TeamStatus;
import me.connor.frcscouting.tabs.teams.info.tabs.info.items.CategoryItem;

public class Team implements Parcelable, Item
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

	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public void setTeamNumber(int teamNumber)
	{
		this.teamNumber = teamNumber;
	}

	public int getTeamNumber()
	{
		return teamNumber;
	}

	public void setTeamName(String teamName)
	{
		this.teamName = teamName;
	}

	public String getTeamName()
	{
		return teamName;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
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

	public void setCategories(List<CategoryItem> categories)
	{
		this.categories = categories;
	}

	public void deleteCategory(int catId)
	{
		List<CategoryItem> cats = new ArrayList<>();

		for (CategoryItem category : getCategories())
		{
			if (category.getCategoryId() != catId)
			{
				cats.add(category);
			}
		}

		setCategories(cats);
	}

	public TeamStatus getOffenseStatus()
	{
		CategoryItem category = getCategory("Offense");

        //Certain teams not loading their category score...

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
    public String toString()
    {
        return getTeamName() + " " + getTeamNumber();
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

    /*
    Database functions.
     */

    public Column[] getDataColumns()
    {
        return Teams.Columns.values();
    }

    public void updateDb(SQLiteDatabase db, ContentValues values)
    {
        db.update(Teams.TABLE, values, Teams.Columns.TEAM_ID + " = ?", new String[]{"" + getId()});
    }

    public int insertDb(SQLiteDatabase db, ContentValues values)
    {
        return (int) db.insert(Teams.TABLE, null, values);
    }
}
