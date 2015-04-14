package me.connor.frcscouting.matches;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MatchesHeaderItem implements MItem
{
	private Date matchTime;

	public MatchesHeaderItem(Date matchTime)
	{
		this.matchTime = matchTime;
	}

	public Date getMatchTime()
	{
		return matchTime;
	}

	@Override
	public boolean isHeader()
	{
		return true;
	}

	public String toString()
	{
		DateFormat df = new SimpleDateFormat("MMM dd 'at' h:mm a");

		return df.format(getMatchTime());
	}
}
