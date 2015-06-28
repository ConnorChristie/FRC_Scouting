package me.connor.frcscouting.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.connor.frcscouting.interfaces.IMatch;
import me.connor.frcscouting.tabs.matches.Match;

public class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "frc_scouting.db";
	public static final int DATABASE_VERSION = 14;

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

    public static final String TABLE_MATCHES = "matches";
    public static final String COLUMN_MATCH_ID = "id";
    public static final String COLUMN_MATCH_KEY = "match_key";
    public static final String COLUMN_MATCH_TYPE = "match_type";
    public static final String COLUMN_MATCH_NUMBER = "match_number";
    public static final String COLUMN_MATCH_TIME = "match_time";
    public static final String COLUMN_MATCH_RED_ALLIANCE = "match_red_alliance";
    public static final String COLUMN_MATCH_BLUE_ALLIANCE = "match_blue_alliance";

    public static class Matches
    {
        public static final String TABLE = "matches";

        public enum Columns
        {
            MATCH_ID(new IMatch()
            {
                public String value(Match m) { return Integer.toString(m.getId()); }
            }),
            MATCH_KEY(new IMatch()
            {
                public String value(Match m) { return m.getMatchKey(); }
            }),
            MATCH_TYPE(new IMatch()
            {
                public String value(Match m) { return m.getHeader().getMatchType(); }
            }),
            MATCH_NUMBER(new IMatch()
            {
                public String value(Match m) { return Integer.toString(m.getHeader().getMatchNumber()); }
            }),
            MATCH_TIME(new IMatch()
            {
                public String value(Match m) { return m.getHeader().getMatchTime(); }
            }),
            MATCH_RED_ALLIANCE(new IMatch()
            {
                public String value(Match m) { return null; }
            }),
            MATCH_BLUE_ALLIANCE(new IMatch()
            {
                public String value(Match m) { return null; }
            });

            private IMatch iMatch;

            Columns(IMatch iMatch)
            {
                this.iMatch = iMatch;
            }

            public String getValue(Match match)
            {
                return iMatch.value(match);
            }
        }
    }

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
        String[] createString = new String[] {
                "CREATE TABLE " + TABLE_TEAMS + "("
                        + COLUMN_TEAM_ID + " integer primary key autoincrement, "
                        + COLUMN_TEAM_NAME + " varchar(255) not null, "
                        + COLUMN_TEAM_NUMBER + " integer, "
                        + COLUMN_TEAM_COMMENTS + " text not null);",
                "CREATE TABLE " + TABLE_CATEGORIES + "("
                        + COLUMN_CATEGORY_ID + " integer primary key autoincrement, "
                        + COLUMN_CATEGORY_TEAM + " integer, "
                        + COLUMN_CATEGORY_LIST_ID + " integer, "
                        + COLUMN_CATEGORY_SCORE + " integer);",
                "CREATE TABLE " + TABLE_CATEGORIES_LIST + "("
                        + COLUMN_CATEGORY_ITEM_ID + " integer primary key autoincrement, "
                        + COLUMN_CATEGORY_ITEM_NAME + " varchar(255), "
                        + COLUMN_CATEGORY_ITEM_DEFAULT_SCORE + " integer);",
                "CREATE TABLE " + TABLE_MATCHES + "("
                        + COLUMN_MATCH_ID + " integer primary key autoincrement, "
                        + COLUMN_MATCH_KEY + " varchar(32) not null, "
                        + COLUMN_MATCH_TYPE + " varchar(255) not null, "
                        + COLUMN_MATCH_NUMBER + " integer, "
                        + COLUMN_MATCH_TIME + " integer, "
                        + COLUMN_MATCH_RED_ALLIANCE + " text not null, "
                        + COLUMN_MATCH_BLUE_ALLIANCE + " text not null);"
        };

        for (String createStr : createString) db.execSQL(createStr);
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
