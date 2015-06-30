package me.connor.frcscouting.database.models;

import me.connor.frcscouting.database.ModelB;
import me.connor.frcscouting.interfaces.ColumnB;
import me.connor.frcscouting.interfaces.IValue;
import me.connor.frcscouting.tabs.teams.info.tabs.info.items.CategoryListItem;

public class CategoriesList implements ModelB
{
    public static final String TABLE = "categories_list";

    public String getTable()
    {
        return TABLE;
    }

    public String getCreateString()
    {
        return "CREATE TABLE " + TABLE + "("
                + Columns.CATEGORY_ITEM_ID + " integer primary key autoincrement, "
                + Columns.CATEGORY_ITEM_NAME + " varchar(255), "
                + Columns.CATEGORY_ITEM_DEFAULT_SCORE + " integer);";
    }

    public enum Columns implements ColumnB<CategoryListItem>
    {
        CATEGORY_ITEM_ID(true, "id", new IValue<CategoryListItem>()
        {
            public String value(CategoryListItem t) { return Integer.toString(t.getId()); }
        }),
        CATEGORY_ITEM_NAME(false, "category_name", new IValue<CategoryListItem>()
        {
            public String value(CategoryListItem t) { return t.getName(); }
        }),
        CATEGORY_ITEM_DEFAULT_SCORE(false, "category_score", new IValue<CategoryListItem>()
        {
            public String value(CategoryListItem t) { return Integer.toString(t.getDefaultScore()); }
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

        public String getValue(CategoryListItem c)
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
