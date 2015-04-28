package me.connor.frcscouting.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.connor.frcscouting.tabs.team_tab.info_view.tabs.info_tab.table_items.CategoryItem;
import me.connor.frcscouting.tabs.team_tab.Team;

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

	public CategoryItem addCategory(int teamId, String name, int score)
	{
		CategoryItem category = new CategoryItem(teamId, 0, name, score);

		saveCategory(category);

		return category;
	}

	public void saveTeam(Team team)
	{
		ContentValues values = new ContentValues();

		values.put(DatabaseHelper.COLUMN_TEAM_ID, team.getId());
		values.put(DatabaseHelper.COLUMN_TEAM_NAME, team.getTeamName());
		values.put(DatabaseHelper.COLUMN_TEAM_NUMBER, team.getTeamNumber());
		values.put(DatabaseHelper.COLUMN_TEAM_COMMENTS, team.getComments());

		int id = (int) db.insertWithOnConflict(DatabaseHelper.TABLE_TEAMS, null, values, SQLiteDatabase.CONFLICT_REPLACE);

		//team.setId(id);
	}

	public void saveCategory(CategoryItem category)
	{
		ContentValues values = new ContentValues();

		values.put(DatabaseHelper.COLUMN_CATEGORY_TEAM, category.getTeamId());
		values.put(DatabaseHelper.COLUMN_CATEGORY_LIST_ID, category.getCategoryId());
		values.put(DatabaseHelper.COLUMN_CATEGORY_SCORE, category.getScore());

		if (category.getId() != -1)
		{
			db.update(DatabaseHelper.TABLE_CATEGORIES, values, "id = ?", new String[] { category.getId() + "" });
		} else
		{
			int id = (int) db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values);

			category.setId(id);
		}
	}

	public void addCategoryItem(String name, int defaultScore)
	{
		ContentValues values = new ContentValues();

		values.put(DatabaseHelper.COLUMN_CATEGORY_ITEM_NAME, name);
		values.put(DatabaseHelper.COLUMN_CATEGORY_ITEM_DEFAULT_SCORE, defaultScore);

		db.insert(DatabaseHelper.TABLE_CATEGORIES_LIST, null, values);
	}

	public List<Team> getAllTeams()
	{
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

		String query = DatabaseHelper.TABLE_TEAMS + " LEFT JOIN " + DatabaseHelper.TABLE_CATEGORIES + " ON " + DatabaseHelper.TABLE_TEAMS + "." + DatabaseHelper.COLUMN_TEAM_ID + " = " + DatabaseHelper.TABLE_CATEGORIES + "." + DatabaseHelper.COLUMN_CATEGORY_TEAM + " LEFT JOIN " + DatabaseHelper.TABLE_CATEGORIES_LIST + " ON " + DatabaseHelper.TABLE_CATEGORIES + "." + DatabaseHelper.COLUMN_CATEGORY_LIST_ID + " = " + DatabaseHelper.TABLE_CATEGORIES_LIST + "." + DatabaseHelper.COLUMN_CATEGORY_ITEM_ID;

		builder.setTables(query);

		Cursor cursor = builder.query(db, null, null, null, null, null, DatabaseHelper.TABLE_TEAMS + "." + DatabaseHelper.COLUMN_TEAM_ID + " DESC");
		cursor.moveToFirst();

		Map<Integer, Team> teams = new LinkedHashMap<>();

		while (!cursor.isAfterLast())
		{
			Team team;

			if (teams.containsKey(cursor.getInt(0)))
			{
				team = teams.get(cursor.getInt(0));
				team.addCategory(new CategoryItem(cursor.getInt(5), cursor.getInt(6), cursor.getString(9), cursor.getInt(7)));
			} else
			{
				team = new Team(cursor.getInt(0), cursor.getInt(2), cursor.getString(1), cursor.getString(3));

				if (cursor.getString(6) != null)
					team.addCategory(new CategoryItem(cursor.getInt(5), cursor.getInt(6), cursor.getString(9), cursor.getInt(7)));

				teams.put(cursor.getInt(0), team);
			}

			cursor.moveToNext();
		}

		cursor.close();

		return new ArrayList(teams.values());
	}
}
