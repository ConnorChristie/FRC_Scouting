package me.connor.frcscouting.tabs.teams.info.tabs.info.items;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import me.connor.frcscouting.R;
import me.connor.frcscouting.database.models.Categories;
import me.connor.frcscouting.interfaces.ColumnB;
import me.connor.frcscouting.interfaces.Item;
import me.connor.frcscouting.listadapter.ListItem;
import me.connor.frcscouting.tabs.teams.info.tabs.info.TeamInfoFragment;
import me.connor.frcscouting.tabs.teams.info.tabs.info.adapter.CategoryListAdapter;

public class CategoryItem extends ListItem implements Parcelable, Item
{
	private View view;

	private int id;
	private int teamId;

	private int categoryId;
	private String category;
	private int score;

	public CategoryItem(int id, int teamId, int categoryId, String category, int score)
	{
		super(R.layout.team_info_list_item);

		this.id = id;
		this.teamId = teamId;

		this.categoryId = categoryId;
		this.category = category;
		this.score = score;
	}

	@Override
	public View populate(View v, LayoutInflater li)
	{
		EditText score;

		if (view == null)
		{
			view = li.inflate(layout, null);

			final boolean isDefaultCategory = category.equalsIgnoreCase("Offense") || category.equalsIgnoreCase("Defense");

			final SwipeLayout sl = (SwipeLayout) view.findViewById(R.id.swipeLayout);
			sl.setShowMode(SwipeLayout.ShowMode.LayDown);

			Button deleteButton = (Button) view.findViewById(R.id.delete);
			deleteButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					if (!isDefaultCategory)
					{
						new AlertDialog.Builder(v.getContext())
								.setTitle("Delete Category")
								.setMessage("Deleting this category will delete it for every other team as well. Are you sure you want to delete it?")
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										sl.close();

										((CategoryListAdapter) TeamInfoFragment.getStatsList().getAdapter()).deleteCategory(categoryId);
									}
								})
								.setNegativeButton("No", null)
								.show();
					} else
					{
						sl.close();

						new AlertDialog.Builder(v.getContext())
								.setTitle("Cannot Delete Category")
								.setMessage("You are unable to delete the default categories.")
								.setNegativeButton("OK", null)
								.show();
					}
				}
			});

			score = (EditText) view.findViewById(R.id.score);
			final EditText sc = score;

			score.setOnEditorActionListener(new TextView.OnEditorActionListener()
			{
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
				{
					if (actionId == EditorInfo.IME_ACTION_DONE)
					{
						sc.clearFocus();

						InputMethodManager in = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						in.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

						return true;
					}

					return false;
				}
			});
		} else
		{
			score = (EditText) view.findViewById(R.id.score);
		}

		TextView category = (TextView) view.findViewById(R.id.category);

		category.setText(getCategory());
		score.setText(getScore() + "");

		return view;
	}

	public void setId(int id)
	{
		this.id = id;
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

	public int getCategoryId()
	{
		return categoryId;
	}

	public int getScore()
	{
		return score;
	}

	public void setEditedScore()
	{
		score = Integer.parseInt(((EditText) view.findViewById(R.id.score)).getText().toString());
	}

    @Override
    public String toString()
    {
        return getCategory();
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

		dest.writeInt(categoryId);
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

		categoryId = source.readInt();
		category = source.readString();
		score = source.readInt();
	}

    /*
    Database functions.
     */

    public ColumnB[] getDataColumns()
    {
        return Categories.Columns.values();
    }

    public void updateDb(SQLiteDatabase db, ContentValues values)
    {
        db.update(Categories.TABLE, values, Categories.Columns.CATEGORY_ID + " = ?", new String[]{"" + getId()});
    }

    public int insertDb(SQLiteDatabase db, ContentValues values)
    {
        return (int) db.insert(Categories.TABLE, null, values);
    }
}
