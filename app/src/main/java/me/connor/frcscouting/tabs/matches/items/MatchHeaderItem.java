package me.connor.frcscouting.tabs.matches.items;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListItem;

public class MatchHeaderItem extends ListItem
{
    private String matchType;
    private int matchNumber;
    private String matchTime;

	public MatchHeaderItem(String matchTitle, int matchNumber, String matchTime)
	{
		super(R.layout.match_list_header);

        this.matchType = matchTitle;
        this.matchNumber = matchNumber;
        this.matchTime = matchTime;
	}

	public View populate(View view, LayoutInflater li)
	{
		if (view == null)
		{
			view = super.populate(view, li);
		}

		TextView section = (TextView) view.findViewById(R.id.list_header_title);
		section.setText(getMatchType() + " " + getMatchNumber() + " - " + getMatchTime());

		return view;
	}

	/*
	public Date getMatchTime()
	{
		return matchTime;
	}

	public String getMatchTimeString()
	{
		DateFormat df = new SimpleDateFormat("MMM dd 'at' h:mm a");

		return df.format(getMatchTime());
	}
	*/

    public String getMatchType()
    {
        return matchType;
    }

    public int getMatchNumber()
    {
        return matchNumber;
    }

    public String getMatchTime()
    {
        return matchTime;
    }

    @Override
    public String toString()
    {
        return getMatchType() + " " + getMatchNumber() + " " + getMatchTime();
    }

    @Override
	public boolean isHeader()
	{
		return true;
	}
}
