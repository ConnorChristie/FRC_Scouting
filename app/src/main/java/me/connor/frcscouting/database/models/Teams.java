package me.connor.frcscouting.database.models;

import me.connor.frcscouting.database.ModelB;
import me.connor.frcscouting.interfaces.ColumnB;
import me.connor.frcscouting.interfaces.IValue;
import me.connor.frcscouting.tabs.teams.Team;

public class Teams implements ModelB
{
    public static final String TABLE = "teams";

    public String getTable()
    {
        return TABLE;
    }

    public String getCreateString()
    {
        return "CREATE TABLE " + TABLE + "("
                + Columns.TEAM_ID + " integer primary key autoincrement, "
                + Columns.TEAM_NAME + " varchar(255) not null, "
                + Columns.TEAM_NUMBER + " integer, "
                + Columns.TEAM_COMMENTS + " text not null);";
    }

    public enum Columns implements ColumnB<Team>
    {
        TEAM_ID(true, "id", new IValue<Team>()
        {
            public String value(Team t) { return Integer.toString(t.getId()); }
        }),
        TEAM_NAME(false, "team_name", new IValue<Team>()
        {
            public String value(Team t) { return t.getTeamName(); }
        }),
        TEAM_NUMBER(false, "team_number", new IValue<Team>()
        {
            public String value(Team t) { return Integer.toString(t.getTeamNumber()); }
        }),
        TEAM_COMMENTS(false, "comments", new IValue<Team>()
        {
            public String value(Team t) { return t.getComments(); }
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

        public String getValue(Team team)
        {
            return iValue.value(team);
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
