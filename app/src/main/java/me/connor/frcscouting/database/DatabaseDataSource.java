package me.connor.frcscouting.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import me.connor.frcscouting.MainActivity;
import me.connor.frcscouting.database.models.Categories;
import me.connor.frcscouting.database.models.CategoriesList;
import me.connor.frcscouting.database.models.Matches;
import me.connor.frcscouting.database.models.Teams;
import me.connor.frcscouting.interfaces.Column;
import me.connor.frcscouting.interfaces.Item;
import me.connor.frcscouting.tabs.matches.attributes.Alliance;
import me.connor.frcscouting.tabs.matches.Match;
import me.connor.frcscouting.tabs.matches.attributes.Side;
import me.connor.frcscouting.tabs.matches.items.MatchHeaderItem;
import me.connor.frcscouting.tabs.matches.items.MatchTeamItem;
import me.connor.frcscouting.tabs.teams.info.tabs.info.items.CategoryItem;
import me.connor.frcscouting.tabs.teams.Team;
import me.connor.frcscouting.tabs.teams.info.tabs.info.items.CategoryListItem;

public class DatabaseDataSource
{
    private MainActivity activity;

	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;

	public DatabaseDataSource(Context context)
	{
		dbHelper = new DatabaseHelper(context);
        activity = MainActivity.getInstance();
	}

	public void open()
	{
		db = dbHelper.getWritableDatabase();
	}

	public <T extends Item> T add(T item)
    {
        ContentValues values = new ContentValues();

        for (Column column : item.getDataColumns())
        {
            if (!column.isId())
            {
                values.put(column.toString(), column.getValue(item));
            }
        }

        item.setId(item.insertDb(db, values));

        return item;
    }

    public <T extends Item> void save(T item)
    {
        ContentValues values = new ContentValues();

        for (Column column : item.getDataColumns())
        {
            if (!column.isId())
            {
                values.put(column.toString(), column.getValue(item));
            }
        }

        item.updateDb(db, values);
    }

    public void addNewTeam(Team team, boolean addDefaults)
    {
        add(team);

        if (addDefaults)
        {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(CategoriesList.TABLE);

            Cursor cursor = builder.query(db, null, null, null, null, null, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast())
            {
                CategoryItem catItem = new CategoryItem(0, team.getId(), cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
                team.addCategory(catItem);

                add(catItem);

                cursor.moveToNext();
            }

            cursor.close();
        }
    }

    public void deleteTeam(Team team)
    {
        db.delete(Categories.TABLE, Categories.Columns.CATEGORY_TEAM + " = ?", new String[]{"" + team.getId()});
        db.delete(Teams.TABLE, Teams.Columns.TEAM_ID + " = ?", new String[]{"" + team.getId()});

        activity.getTeamsMap().remove(team.getTeamNumber());
    }

    public void deleteCategory(int catId)
    {
        db.delete(Categories.TABLE, Categories.Columns.CATEGORY_LIST_ID + " = ?", new String[]{"" + catId});
        db.delete(CategoriesList.TABLE, CategoriesList.Columns.CATEGORY_ITEM_ID + " = ?", new String[]{"" + catId});
    }

    public void populateCategoryItems()
    {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(CategoriesList.TABLE);

        Cursor cursor = builder.query(db, null, null, null, null, null, null);
        cursor.moveToFirst();

        boolean hasOffenseCat = false;
        boolean hasDefenseCat = false;

        while (!cursor.isAfterLast())
        {
            if (cursor.getString(1).equalsIgnoreCase("Offense"))
            {
                hasOffenseCat = true;
            } else if (cursor.getString(1).equalsIgnoreCase("Defense"))
            {
                hasDefenseCat = true;
            }

			cursor.moveToNext();
        }

		cursor.close();

        if (!hasOffenseCat) add(new CategoryListItem("Offense", 0));
        if (!hasDefenseCat) add(new CategoryListItem("Defense", 0));
    }

    public List<Match> getAllMatches()
    {
        List<Match> matches = new ArrayList<>();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(Matches.TABLE);

        Cursor cursor = builder.query(db, null, null, null, null, null, Matches.TABLE + "." + Matches.Columns.MATCH_ID + " ASC");
        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            Map<Integer, MatchTeamItem> matchTeams = new LinkedHashMap<>();

            for (String s : cursor.getString(cursor.getColumnIndex(Matches.Columns.MATCH_RED_ALLIANCE.toString())).split(", "))
            {
                String[] teamSide = s.split(Pattern.quote("|"));

                matchTeams.put(Integer.parseInt(teamSide[0]), new MatchTeamItem(activity.getTeam(Integer.parseInt(teamSide[0])), Alliance.RED, Side.fromText(teamSide[1]))); //Red Alliance
            }

            for (String s : cursor.getString(cursor.getColumnIndex(Matches.Columns.MATCH_BLUE_ALLIANCE.toString())).split(", "))
            {
                String[] teamSide = s.split(Pattern.quote("|"));

                matchTeams.put(Integer.parseInt(teamSide[0]), new MatchTeamItem(activity.getTeam(Integer.parseInt(teamSide[0])), Alliance.BLUE, Side.fromText(teamSide[1]))); //Blue Alliance
            }

            matches.add(new Match(cursor.getInt(cursor.getColumnIndex(Matches.Columns.MATCH_ID.toString())), cursor.getString(cursor.getColumnIndex(Matches.Columns.MATCH_KEY.toString())), new MatchHeaderItem(cursor.getString(cursor.getColumnIndex(Matches.Columns.MATCH_TYPE.toString())), cursor.getInt(cursor.getColumnIndex(Matches.Columns.MATCH_NUMBER.toString())), cursor.getString(cursor.getColumnIndex(Matches.Columns.MATCH_TIME.toString()))), matchTeams));

            cursor.moveToNext();
        }

        cursor.close();

        return matches;
    }

	public Map<Integer, Team> getAllTeams()
	{
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

		String query = Teams.TABLE + " LEFT JOIN " + Categories.TABLE + " ON " + Teams.TABLE + "." + Teams.Columns.TEAM_ID + " = " + Categories.TABLE + "." + Categories.Columns.CATEGORY_TEAM + " LEFT JOIN " + CategoriesList.TABLE + " ON " + Categories.TABLE + "." + Categories.Columns.CATEGORY_LIST_ID + " = " + CategoriesList.TABLE + "." + CategoriesList.Columns.CATEGORY_ITEM_ID;

		builder.setTables(query);

		Cursor cursor = builder.query(db, null, null, null, null, null, Teams.TABLE + "." + Teams.Columns.TEAM_NUMBER + " ASC");
		cursor.moveToFirst();

		Map<Integer, Team> teams = new LinkedHashMap<>();

		while (!cursor.isAfterLast())
		{
			Team team;

			if (teams.containsKey(cursor.getInt(2)))
			{
				team = teams.get(cursor.getInt(2));
				team.addCategory(new CategoryItem(cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getString(9), cursor.getInt(7)));
			} else
			{
				team = new Team(cursor.getInt(0), cursor.getInt(2), cursor.getString(1), cursor.getString(3));

				if (cursor.getString(6) != null)
					team.addCategory(new CategoryItem(cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getString(9), cursor.getInt(7)));

				teams.put(cursor.getInt(2), team);
			}

			cursor.moveToNext();
		}

		cursor.close();

		return teams;
	}
}
