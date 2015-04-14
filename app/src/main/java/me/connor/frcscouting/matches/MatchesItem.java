package me.connor.frcscouting.matches;

public class MatchesItem implements MItem
{
	private int teamId;
	private String teamNickname;
	private boolean isAlliance;
	private TeamStatus status;

	public MatchesItem(int teamId, String teamNickname, boolean isAlliance, TeamStatus status)
	{
		this.teamId = teamId;
		this.teamNickname = teamNickname;
		this.isAlliance = isAlliance;
		this.status = status;
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

	@Override
	public boolean isHeader()
	{
		return false;
	}
}
