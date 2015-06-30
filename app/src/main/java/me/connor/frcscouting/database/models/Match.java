package me.connor.frcscouting.database.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import me.connor.frcscouting.interfaces.ColumnB;
import me.connor.frcscouting.interfaces.IValue;
import me.connor.frcscouting.interfaces.Item;
import me.connor.frcscouting.tabs.matches.MatchB;
import me.connor.frcscouting.tabs.matches.attributes.Alliance;
import me.connor.frcscouting.tabs.matches.items.MatchHeaderItem;
import me.connor.frcscouting.tabs.matches.items.MatchTeamItem;

@Table(name = "matches")
public class Match extends Model implements Comparable<Match>
{
    @Column(name = "key")
    public String key;
    @Column(name = "type")
    public String type;

    @Column(name = "number")
    public int number;
    @Column(name = "time")
    public String time;
    @Column(name = "completed")
    public boolean completed;

    @Column(name = "red_alliance")
    public List<String> redAlliance;
    @Column(name = "blue_alliance")
    public List<String> blueAlliance;

    private MatchHeaderItem header;
    private Map<Integer, MatchTeamItem> matchTeams;

    public Match()
    {
        super();
    }

    public Match(String key, String type, int number, String time, boolean completed, List<String> redAlliance, List<String> blueAlliance)
    {
        super();

        this.key = key;
        this.type = type;

        this.number = number;
        this.time = time;
        this.completed = completed;

        this.redAlliance = redAlliance;
        this.blueAlliance = blueAlliance;
    }

    public MatchHeaderItem getHeader()
    {
        return header;
    }

    public List<MatchTeamItem> getMatchTeams()
    {
        return new ArrayList<>(matchTeams.values());
    }

    @Override
    public int compareTo(Match another)
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
}
