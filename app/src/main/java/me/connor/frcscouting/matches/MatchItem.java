package me.connor.frcscouting.matches;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListItem;

public class MatchItem extends ListItem
{
	private int teamId;
	private String teamNickname;
	private boolean isAlliance;
	private TeamStatus status;
	private String side;

	public MatchItem(int teamId, String teamNickname, boolean isAlliance, TeamStatus status, String side)
	{
		this.layout = R.layout.list_item;
		this.teamId = teamId;
		this.teamNickname = teamNickname;
		this.isAlliance = isAlliance;
		this.status = status;
		this.side = side;
	}

	public View populate(View view, LayoutInflater li)
	{
		view = super.populate(view, li);

		TextView name = (TextView) view.findViewById(R.id.list_item_title);
		TextView subtitle = (TextView) view.findViewById(R.id.list_item_subtitle);
		TextView status = (TextView) view.findViewById(R.id.list_item_status);
		TextView side = (TextView) view.findViewById(R.id.list_item_side);

		if (name != null)
		{
			name.setText(getTeamNickname());
			subtitle.setText(getTeamId() + "");
			side.setText(getSide());

			if (getSide().equals("OFFENSE"))
			{
				side.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			} else
			{
				side.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
			}

			status.setText(getStatus().getTitle());
			status.setTextColor(getStatus().getColor());

			if (isAlliance())
			{
				view.setBackgroundColor(Color.parseColor("#0a048b03"));
			} else
			{
				view.setBackgroundColor(Color.parseColor("#0a8b0002"));
			}
		}

		return view;
	}

	public int getTeamId()
	{
		return teamId;
	}

	public String getTeamNickname()
	{
		return teamNickname;
	}

	public boolean isAlliance()
	{
		return isAlliance;
	}

	public TeamStatus getStatus()
	{
		return status;
	}

	public String getSide()
	{
		return side;
	}

	public void setSide(String side)
	{
		this.side = side;
	}

	@Override
	public boolean isHeader()
	{
		return false;
	}
}
