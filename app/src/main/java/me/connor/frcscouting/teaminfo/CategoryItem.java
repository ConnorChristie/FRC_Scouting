package me.connor.frcscouting.teaminfo;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
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
		//view = super.populate(view, li);

		EditText score;

		if (view == null)
		{
			view = li.inflate(layout, null);

			final SwipeLayout sl = (SwipeLayout) view.findViewById(R.id.swipeLayout);
			sl.setShowMode(SwipeLayout.ShowMode.LayDown);

			Button deleteButton = (Button) view.findViewById(R.id.delete);
			deleteButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					sl.close(true);
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
