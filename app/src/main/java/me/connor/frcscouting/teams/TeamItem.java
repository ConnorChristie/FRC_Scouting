package me.connor.frcscouting.teams;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListItem;

public class TeamItem extends ListItem
{
	private Team team;

	public TeamItem(Team team)
	{
		super(R.layout.team_list_item);

		this.team = team;
	}

	public View populate(View view, LayoutInflater li)
	{
		view = super.populate(view, li);

		TextView name = (TextView) view.findViewById(R.id.team_item_title);
		TextView subtitle = (TextView) view.findViewById(R.id.team_item_subtitle);

		TextView offense = (TextView) view.findViewById(R.id.team_item_offense);
		TextView defense = (TextView) view.findViewById(R.id.team_item_defense);

		//TextView side = (TextView) view.findViewById(R.id.team_item_side);

		if (name != null)
		{
			name.setText(team.getTeamName());
			subtitle.setText(team.getTeamNumber() + "");

			offense.setText(team.getOffenseScore().getTitle());
			offense.setTextColor(team.getOffenseScore().getColor());

			defense.setText(team.getDefenseScore().getTitle());
			defense.setTextColor(team.getDefenseScore().getColor());
		}

		return view;
	}

	public Team getTeam()
	{
		return team;
	}

	@Override
	public boolean isHeader()
	{
		return false;
	}
}