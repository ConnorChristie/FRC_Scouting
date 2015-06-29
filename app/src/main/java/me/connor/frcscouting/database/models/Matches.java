package me.connor.frcscouting.database.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.connor.frcscouting.database.Model;
import me.connor.frcscouting.interfaces.Column;
import me.connor.frcscouting.interfaces.IValue;
import me.connor.frcscouting.tabs.matches.Match;
import me.connor.frcscouting.tabs.matches.attributes.Alliance;
import me.connor.frcscouting.tabs.matches.items.MatchTeamItem;

public class Matches implements Model
{
    public static final String TABLE = "matches";

    public String getTable()
    {
        return TABLE;
    }

    public String getCreateString()
    {
        return "CREATE TABLE " + TABLE + "("
                + Columns.MATCH_ID + " integer primary key autoincrement, "
                + Columns.MATCH_KEY + " varchar(32) not null, "
                + Columns.MATCH_TYPE + " varchar(255) not null, "
                + Columns.MATCH_NUMBER + " integer, "
                + Columns.MATCH_TIME + " integer, "
                + Columns.MATCH_RED_ALLIANCE + " text not null, "
                + Columns.MATCH_BLUE_ALLIANCE + " text not null);";
    }

    public enum Columns implements Column<Match>
    {
        MATCH_ID(true, "id", new IValue<Match>()
        {
            public String value(Match m) { return Integer.toString(m.getId()); }
        }),
        MATCH_KEY(false, "match_key", new IValue<Match>()
        {
            public String value(Match m) { return m.getMatchKey(); }
        }),
        MATCH_TYPE(false, "match_type", new IValue<Match>()
        {
            public String value(Match m) { return m.getHeader().getMatchType(); }
        }),
        MATCH_NUMBER(false, "match_number", new IValue<Match>()
        {
            public String value(Match m) { return Integer.toString(m.getHeader().getMatchNumber()); }
        }),
        MATCH_TIME(false, "match_time", new IValue<Match>()
        {
            public String value(Match m) { return m.getHeader().getMatchTime(); }
        }),
        MATCH_RED_ALLIANCE(false, "match_red_alliance", new IValue<Match>()
        {
            public String value(Match m) {
                List<String> teams = new ArrayList<>();

                for (MatchTeamItem teamItem : m.getMatchTeams())
                    if (teamItem.getAlliance() == Alliance.RED)
                        teams.add(teamItem.getTeam().getTeamNumber() + "|" + teamItem.getSide());

                return Arrays.toString(teams.toArray(new String[teams.size()])).replace("[", "").replace("]", "");
            }
        }),
        MATCH_BLUE_ALLIANCE(false, "match_blue_alliance", new IValue<Match>()
        {
            public String value(Match m) {
                List<String> teams = new ArrayList<>();

                for (MatchTeamItem teamItem : m.getMatchTeams())
                    if (teamItem.getAlliance() == Alliance.BLUE)
                        teams.add(teamItem.getTeam().getTeamNumber() + "|" + teamItem.getSide());

                return Arrays.toString(teams.toArray(new String[teams.size()])).replace("[", "").replace("]", "");
            }
        });

        private boolean isId;
        private String columnName;

        private IValue iValue;

        Columns(boolean isId, String columnName, IValue iValue)
        {
            this.isId = isId;
            this.columnName = columnName;
            this.iValue = iValue;
        }

        public String getValue(Match match)
        {
            return iValue.value(match);
        }

        public boolean isId()
        {
            return isId;
        }

        public String toString()
        {
            return columnName;
        }
    }
}
