package me.connor.frcscouting.matches;

public class MatchesItem implements MItem
{
	private int teamId;
	private boolean isAlliance;

	public MatchesItem(int teamId, boolean isAlliance)
	{
		this.teamId = teamId;
		this.isAlliance = isAlliance;
	}

	public int getTeamId()
	{
		return teamId;
	}

	public boolean isAlliance()
	{
		return isAlliance;
	}

	@Override
	public boolean isHeader()
	{
		return false;
	}

	public String toString()
	{
		return "" + getTeamId();
	}
}
