package me.connor.frcscouting.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "frc_scouting.db";
	public static final int DATABASE_VERSION = 10;

	public static final String TABLE_TEAMS = "teams";
	public static final String COLUMN_TEAM_ID = "id";
	public static final String COLUMN_TEAM_NAME = "team_name";
	public static final String COLUMN_TEAM_NUMBER = "team_number";
	public static final String COLUMN_TEAM_COMMENTS = "comments";

	public static final String TABLE_CATEGORIES = "categories";
	public static final String COLUMN_CATEGORY_ID = "id";
	public static final String COLUMN_CATEGORY_TEAM = "team_id";
	public static final String COLUMN_CATEGORY_LIST_ID = "category_item_id";
	public static final String COLUMN_CATEGORY_SCORE = "category_score";

	public static final String TABLE_CATEGORIES_LIST = "categories_list";
	public static final String COLUMN_CATEGORY_ITEM_ID = "id";
	public static final String COLUMN_CATEGORY_ITEM_NAME = "category_name";
	public static final String COLUMN_CATEGORY_ITEM_DEFAULT_SCORE = "category_score";

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String createString = "CREATE TABLE " + TABLE_TEAMS + "("
				+ COLUMN_TEAM_ID + " integer primary key autoincrement, "
				+ COLUMN_TEAM_NAME + " varchar(255) not null, "
				+ COLUMN_TEAM_NUMBER + " integer, "
				+ COLUMN_TEAM_COMMENTS + " text not null);";

		db.execSQL(createString);

		createString = "CREATE TABLE " + TABLE_CATEGORIES + "("
				+ COLUMN_CATEGORY_ID + " integer primary key autoincrement, "
				+ COLUMN_CATEGORY_TEAM + " integer, "
				+ COLUMN_CATEGORY_LIST_ID + " integer, "
				+ COLUMN_CATEGORY_SCORE + " integer);";

		db.execSQL(createString);

		createString = "CREATE TABLE " + TABLE_CATEGORIES_LIST + "("
				+ COLUMN_CATEGORY_ITEM_ID + " integer primary key autoincrement, "
				+ COLUMN_CATEGORY_ITEM_NAME + " varchar(255), "
				+ COLUMN_CATEGORY_ITEM_DEFAULT_SCORE + " integer);";

		db.execSQL(createString);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (oldVersion != newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES_LIST);

			onCreate(db);
		}
	}
}
