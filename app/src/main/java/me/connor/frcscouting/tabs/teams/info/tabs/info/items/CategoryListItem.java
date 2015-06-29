package me.connor.frcscouting.tabs.teams.info.tabs.info.items;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import me.connor.frcscouting.database.models.CategoriesList;
import me.connor.frcscouting.interfaces.Column;
import me.connor.frcscouting.interfaces.Item;

public class CategoryListItem implements Item
{
    private int id;

    private String name;
    private int defaultScore;

    public CategoryListItem(String name, int defaultScore)
    {
        this.name = name;
        this.defaultScore = defaultScore;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public int getDefaultScore()
    {
        return defaultScore;
    }

    /*
    Database functions.
     */

    public Column[] getDataColumns()
    {
        return CategoriesList.Columns.values();
    }

    public void updateDb(SQLiteDatabase db, ContentValues values)
    {
        db.update(CategoriesList.TABLE, values, CategoriesList.Columns.CATEGORY_ITEM_ID + " = ?", new String[]{"" + getId()});
    }

    public int insertDb(SQLiteDatabase db, ContentValues values)
    {
        return (int) db.insert(CategoriesList.TABLE, null, values);
    }
}
