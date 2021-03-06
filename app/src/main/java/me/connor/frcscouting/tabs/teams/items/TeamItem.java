package me.connor.frcscouting.tabs.teams.items;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListItem;
import me.connor.frcscouting.tabs.teams.Team;

public class TeamItem extends ListItem implements Comparable<TeamItem>
{
	private Team team;

	public TeamItem(Team team)
	{
		super(R.layout.team_list_item);

		this.team = team;
	}

	public TeamItem() { super(R.layout.team_list_item); }

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

			offense.setText(team.getOffenseStatus().getTitle());
			offense.setTextColor(team.getOffenseStatus().getColor());

			defense.setText(team.getDefenseStatus().getTitle());
			defense.setTextColor(team.getDefenseStatus().getColor());
		}

		return view;
	}

	public Team getTeam()
	{
		return team;
	}

    @Override
    public String toString()
    {
        return team.toString();
    }

    @Override
	public boolean isHeader()
	{
		return false;
	}

	@Override
	public int compareTo(TeamItem otherTeam)
	{
		return getTeam().getTeamNumber() > team.getTeamNumber() ? 1 : 0;
	}
}