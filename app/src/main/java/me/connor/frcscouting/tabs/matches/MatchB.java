package me.connor.frcscouting.tabs.matches;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.connor.frcscouting.database.models.Match;
import me.connor.frcscouting.interfaces.ColumnB;
import me.connor.frcscouting.interfaces.Item;
import me.connor.frcscouting.listadapter.ListItem;
import me.connor.frcscouting.tabs.matches.items.MatchHeaderItem;
import me.connor.frcscouting.tabs.matches.items.MatchTeamItem;
import me.connor.frcscouting.tabs.teams.Team;

public class MatchB implements Comparable<MatchB>, Item
{
    private int id;

    private String matchKey;
    private String eventKey;

    private boolean completed;

	private MatchHeaderItem header;
	private Map<Integer, MatchTeamItem> matchTeams;

	public MatchB(int id, String matchKey, String eventKey, MatchHeaderItem header, Map<Integer, MatchTeamItem> teams)
	{
        this.id = id;
        this.matchKey = matchKey;
        this.eventKey = eventKey;

		this.header = header;
		this.matchTeams = teams;
	}

	public MatchHeaderItem getHeader()
	{
		return header;
	}

	public List<MatchTeamItem> getMatchTeams()
	{
		return new ArrayList<>(matchTeams.values());
	}

	public List<ListItem> getListItems()
	{
		List<ListItem> items = new ArrayList<>();

		items.add(header);
		items.addAll(matchTeams.values());

		return items;
	}

    public String getMatchKey()
    {
        return matchKey;
    }

    public String getEventKey()
    {
        return eventKey;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    @Override
    public String toString()
    {
        String str = header.toString();

        for (MatchTeamItem ti : matchTeams.values())
        {
            str += " " + ti.toString();
        }

        return str;
    }

    @Override
    public int compareTo(MatchB another)
    {
        if (!getHeader().getMatchType().equals(another.getHeader().getMatchType()))
        {
            if (getHeader().getMatchType().equals("Qualification"))
            {
                return -1;
            } else if (getHeader().getMatchType().equals("Quarterfinal"))
            {
                if (another.getHeader().getMatchType().equals("Semifinal") || another.getHeader().getMatchType().equals("Final"))
                {
                    return -1;
                } else
                {
                    return 1;
                }
            } else if (getHeader().getMatchType().equals("Semifinal"))
            {
                if (another.getHeader().getMatchType().equals("Final"))
                {
                    return -1;
                } else
                {
                    return 1;
                }
            } else if (getHeader().getMatchType().equals("Final"))
            {
                return 1;
            }
        } else
        {
            return getHeader().getMatchNumber() < another.getHeader().getMatchNumber() ? -1 : (getHeader().getMatchNumber() > another.getHeader().getMatchNumber() ? 1 : 0);
        }

        return 0;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setTeam(Team team)
    {
        if (matchTeams.containsKey(team.getTeamNumber()))
        {
            matchTeams.get(team.getTeamNumber()).setTeam(team);
        }
    }

    /*
    Database functions.
     */

    public ColumnB[] getDataColumns()
    {
        return Match.Columns.values();
    }

    public void updateDb(SQLiteDatabase db, ContentValues values)
    {
        db.update(Match.TABLE, values, Match.Columns.MATCH_ID + " = ?", new String[] { "" + getId() });
    }

    public int insertDb(SQLiteDatabase db, ContentValues values)
    {
        return (int) db.insert(Match.TABLE, null, values);
    }
}
