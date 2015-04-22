package me.connor.frcscouting.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.connor.frcscouting.teaminfo.CategoryItem;
import me.connor.frcscouting.teams.Team;

public class DatabaseDataSource
{
	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;

	public DatabaseDataSource(Context context)
	{
		dbHelper = new DatabaseHelper(context);
	}

	public void open()
	{
		db = dbHelper.getWritableDatabase();
	}

	public void close()
	{
		dbHelper.close();
	}

	public void saveTeam(Team team)
	{
		ContentValues values = new ContentValues();

		values.put(DatabaseHelper.COLUMN_TEAM_NAME, team.getTeamName());
		values.put(DatabaseHelper.COLUMN_TEAM_NUMBER, team.getTeamNumber());
		values.put(DatabaseHelper.COLUMN_TEAM_COMMENTS, team.getComments());

		db.insert(DatabaseHelper.TABLE_TEAMS, null, values);
	}

	public void saveCategory(CategoryItem category)
	{
		ContentValues values = new ContentValues();

		values.put(DatabaseHelper.COLUMN_CATEGORY_TEAM, category.getTeamId());
		values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, category.getCategory());
		values.put(DatabaseHelper.COLUMN_CATEGORY_SCORE, category.getScore());

		db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values);
	}

	public List<Team> getAllTeams()
	{
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

		builder.setTables(DatabaseHelper.TABLE_TEAMS + " LEFT OUTER JOIN " + DatabaseHelper.TABLE_CATEGORIES + " ON " + DatabaseHelper.TABLE_TEAMS + "." + DatabaseHelper.COLUMN_TEAM_ID + " = " + DatabaseHelper.TABLE_CATEGORIES + "." + DatabaseHelper.COLUMN_CATEGORY_TEAM);

		Cursor cursor = builder.query(db, null, null, null, null, null, DatabaseHelper.TABLE_TEAMS + "." + DatabaseHelper.COLUMN_TEAM_ID + " DESC");
		cursor.moveToFirst();

		Map<Integer, Team> teams = new LinkedHashMap<>();

		while (!cursor.isAfterLast())
		{
			Team team;

			if (teams.containsKey(cursor.getInt(0)))
			{
				team = teams.get(cursor.getInt(0));
				team.addCategory(new CategoryItem(cursor.getInt(4), cursor.getInt(5), cursor.getString(6), cursor.getInt(7)));
			} else
			{
				team = new Team(cursor.getInt(0), cursor.getInt(2), cursor.getString(1), cursor.getString(3));

				if (cursor.getString(6) != null)
					team.addCategory(new CategoryItem(cursor.getInt(4), cursor.getInt(5), cursor.getString(6), cursor.getInt(7)));

				teams.put(cursor.getInt(0), team);
			}

			cursor.moveToNext();
		}

		cursor.close();

		return new ArrayList(teams.values());
	}
}
