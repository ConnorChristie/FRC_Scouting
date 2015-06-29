package me.connor.frcscouting.interfaces;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public interface Item
{
    int getId();

    void setId(int id);

    Column[] getDataColumns();

    void updateDb(SQLiteDatabase db, ContentValues values);

    int insertDb(SQLiteDatabase db, ContentValues values);
}
