package me.connor.frcscouting.matches;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListItem;

public class MatchHeaderItem extends ListItem
{
	private Date matchTime;

	public MatchHeaderItem(Date matchTime)
	{
		super(R.layout.match_list_header);

		this.matchTime = matchTime;
	}

	public View populate(View view, LayoutInflater li)
	{
		if (view == null)
		{
			view = super.populate(view, li);
		}

		TextView section = (TextView) view.findViewById(R.id.list_header_title);
		section.setText(getMatchTimeString());

		return view;
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
