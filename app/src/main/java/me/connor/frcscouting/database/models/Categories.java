package me.connor.frcscouting.database.models;

import me.connor.frcscouting.database.ModelB;
import me.connor.frcscouting.interfaces.ColumnB;
import me.connor.frcscouting.interfaces.IValue;
import me.connor.frcscouting.tabs.teams.info.tabs.info.items.CategoryItem;

public class Categories implements ModelB
{
    public static final String TABLE = "categories";

    public String getTable()
    {
        return TABLE;
    }

    public String getCreateString()
    {
        return "CREATE TABLE " + TABLE + "("
                + Columns.CATEGORY_ID + " integer primary key autoincrement, "
                + Columns.CATEGORY_TEAM + " integer, "
                + Columns.CATEGORY_LIST_ID + " integer, "
                + Columns.CATEGORY_SCORE + " integer);";
    }

    public enum Columns implements ColumnB<CategoryItem>
    {
        CATEGORY_ID(true, "id", new IValue<CategoryItem>()
        {
            public String value(CategoryItem t) { return Integer.toString(t.getId()); }
        }),
        CATEGORY_TEAM(false, "team_id", new IValue<CategoryItem>()
        {
            public String value(CategoryItem t) { return Integer.toString(t.getTeamId()); }
        }),
        CATEGORY_LIST_ID(false, "category_item_id", new IValue<CategoryItem>()
        {
            public String value(CategoryItem t) { return Integer.toString(t.getCategoryId()); }
        }),
        CATEGORY_SCORE(false, "category_score", new IValue<CategoryItem>()
        {
            public String value(CategoryItem t) { return Integer.toString(t.getScore()); }
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

        public String getValue(CategoryItem c)
        {
            return iValue.value(c);
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
