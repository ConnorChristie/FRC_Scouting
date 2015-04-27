package me.connor.frcscouting.tabs.match_tab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.connor.frcscouting.listadapter.ListItem;
import me.connor.frcscouting.tabs.match_tab.table_items.MatchHeaderItem;
import me.connor.frcscouting.tabs.match_tab.table_items.MatchTeamItem;

public class Match
{
	private MatchHeaderItem header;
	private List<MatchTeamItem> matchTeams;

	public Match(MatchHeaderItem header, MatchTeamItem[] teams)
	{
		this.header = header;
		this.matchTeams = Arrays.asList(teams);
	}

	public MatchHeaderItem getHeader()
	{
		return header;
	}

	public List<MatchTeamItem> getMatchTeams()
	{
		return matchTeams;
	}

	public List<ListItem> getListItems()
	{
		List<ListItem> items = new ArrayList<>();

		items.add(header);
		items.addAll(matchTeams);

		return items;
	}
}