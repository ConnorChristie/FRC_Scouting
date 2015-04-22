package me.connor.frcscouting.matches;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListItem;
import me.connor.frcscouting.teams.Team;

public class MatchTeamItem extends ListItem
{
	private Team team;
	private boolean isAlliance;
	private String side;

	public MatchTeamItem(Team team, boolean isAlliance, String side)
	{
		super(R.layout.match_list_item);

		this.team = team;
		this.isAlliance = isAlliance;
		this.side = side;
	}

	public View populate(View view, LayoutInflater li)
	{
		if (view == null)
		{
			view = super.populate(view, li);
		}

		TextView name = (TextView) view.findViewById(R.id.match_item_title);
		TextView subtitle = (TextView) view.findViewById(R.id.match_item_subtitle);
		TextView status = (TextView) view.findViewById(R.id.match_item_status);
		TextView side = (TextView) view.findViewById(R.id.match_item_side);

		if (name != null)
		{
			name.setText(team.getTeamName());
			subtitle.setText(team.getTeamNumber() + "");
			side.setText(getSide());

			if (getSide().equals("OFFENSE"))
			{
				status.setText(team.getOffenseStatus().getTitle());
				status.setTextColor(team.getOffenseStatus().getColor());
			} else
			{
				status.setText(team.getDefenseStatus().getTitle());
				status.setTextColor(team.getDefenseStatus().getColor());
			}

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

	public Team getTeam()
	{
		return team;
	}

	public boolean isAlliance()
	{
		return isAlliance;
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
