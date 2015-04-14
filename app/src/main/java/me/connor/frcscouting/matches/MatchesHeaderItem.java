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

	public String getMatchTimeString()
	{
		DateFormat df = new SimpleDateFormat("MMM dd 'at' h:mm a");

		return df.format(getMatchTime());
	}

	@Override
	public boolean isHeader()
	{
		return true;
	}
}
