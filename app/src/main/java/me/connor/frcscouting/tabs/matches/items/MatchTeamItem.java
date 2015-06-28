package me.connor.frcscouting.tabs.matches.items;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListItem;
import me.connor.frcscouting.tabs.matches.attributes.Alliance;
import me.connor.frcscouting.tabs.matches.attributes.Side;
import me.connor.frcscouting.tabs.teams.Team;

public class MatchTeamItem extends ListItem
{
	private Team team;
	private Alliance alliance;
    private Side side;

	public MatchTeamItem(Team team, Alliance alliance, Side side)
	{
		super(R.layout.match_list_item);

		this.team = team;
		this.alliance = alliance;
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
			side.setText(getSide().getText());

			if (getSide() == Side.OFFENSE)
			{
				status.setText(team.getOffenseStatus().getTitle());
				status.setTextColor(team.getOffenseStatus().getColor());
			} else
			{
				status.setText(team.getDefenseStatus().getTitle());
				status.setTextColor(team.getDefenseStatus().getColor());
			}

            view.setBackgroundColor(alliance.getBackgroundColor());
		}

		return view;
	}

    public void setTeam(Team team)
    {
        this.team = team;
    }

	public Team getTeam()
	{
		return team;
	}

	public Side getSide()
	{
		return side;
	}

	public void setSide(Side side)
	{
		this.side = side;
	}

    public Alliance getAlliance()
    {
        return alliance;
    }

    @Override
    public String toString()
    {
        return team.toString() + " " + getSide().toString() + " " + getAlliance().toString();
    }

    @Override
	public boolean isHeader()
	{
		return false;
	}
}
