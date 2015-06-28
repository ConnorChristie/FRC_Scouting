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
import me.connor.frcscouting.tabs.matches.attributes.Alliance;
import me.connor.frcscouting.tabs.matches.Match;
import me.connor.frcscouting.tabs.matches.attributes.Side;
import me.connor.frcscouting.tabs.matches.items.MatchHeaderItem;
import me.connor.frcscouting.tabs.matches.items.MatchTeamItem;
import me.connor.frcscouting.tabs.teams.info.tabs.info.items.CategoryItem;
import me.connor.frcscouting.tabs.teams.Team;

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

	public void close()
	{
		dbHelper.close();
	}

    /**
     * Saves the team into the database.
     *
     * @param team Team to be saved
     */
	public void saveTeam(Team team)
	{
		ContentValues values = new ContentValues();

		values.put(DatabaseHelper.COLUMN_TEAM_ID, team.getId());
		values.put(DatabaseHelper.COLUMN_TEAM_NAME, team.getTeamName());
		values.put(DatabaseHelper.COLUMN_TEAM_NUMBER, team.getTeamNumber());
		values.put(DatabaseHelper.COLUMN_TEAM_COMMENTS, team.getComments());

        db.update(DatabaseHelper.TABLE_TEAMS, values, "id = ?", new String[]{team.getId() + ""});

		//int id = (int) db.insertWithOnConflict(DatabaseHelper.TABLE_TEAMS, null, values, SQLiteDatabase.CONFLICT_REPLACE);

		//team.setId(id);
	}

	public void addTeam(Team team)
    {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_TEAM_NAME, team.getTeamName());
        values.put(DatabaseHelper.COLUMN_TEAM_NUMBER, team.getTeamNumber());
        values.put(DatabaseHelper.COLUMN_TEAM_COMMENTS, team.getComments());

        int id = (int) db.insert(DatabaseHelper.TABLE_TEAMS, null, values);

        team.setId(id);
    }

    public void addNewTeam(Team team, boolean addDefaults)
    {
        addTeam(team);

		if (addDefaults)
		{
			SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
			builder.setTables(DatabaseHelper.TABLE_CATEGORIES_LIST);

			Cursor cursor = builder.query(db, null, null, null, null, null, null);
			cursor.moveToFirst();

			while (!cursor.isAfterLast())
			{
				CategoryItem catItem = new CategoryItem(0, team.getId(), cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
				team.addCategory(catItem);

				addCategory(catItem);

				cursor.moveToNext();
			}

			cursor.close();
		}
    }

	public void deleteTeam(Team team)
	{
		db.delete(DatabaseHelper.TABLE_CATEGORIES, DatabaseHelper.COLUMN_CATEGORY_TEAM + " = ?", new String[]{"" + team.getId()});
		db.delete(DatabaseHelper.TABLE_TEAMS, DatabaseHelper.COLUMN_TEAM_ID + " = ?", new String[]{"" + team.getId()});

        activity.getTeamsMap().remove(team.getTeamNumber());
	}

    public Match addNewMatch(Match match)
    {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_MATCH_KEY, match.getMatchKey());
        values.put(DatabaseHelper.COLUMN_MATCH_TYPE, match.getHeader().getMatchType());
        values.put(DatabaseHelper.COLUMN_MATCH_NUMBER, match.getHeader().getMatchNumber());
        values.put(DatabaseHelper.COLUMN_MATCH_TIME, match.getHeader().getMatchTime());

        List<String> redTeams = new ArrayList<>();
        List<String> blueTeams = new ArrayList<>();

        for (MatchTeamItem teamItem : match.getMatchTeams())
        {
            if (teamItem.getAlliance() == Alliance.RED)
                redTeams.add(teamItem.getTeam().getTeamNumber() + "|" + teamItem.getSide());
            else
                blueTeams.add(teamItem.getTeam().getTeamNumber() + "|" + teamItem.getSide());
        }

        values.put(DatabaseHelper.COLUMN_MATCH_RED_ALLIANCE, Arrays.toString(redTeams.toArray(new String[redTeams.size()])).replace("[", "").replace("]", ""));
        values.put(DatabaseHelper.COLUMN_MATCH_BLUE_ALLIANCE, Arrays.toString(blueTeams.toArray(new String[blueTeams.size()])).replace("[", "").replace("]", ""));

        int id = (int) db.insert(DatabaseHelper.Matches.TABLE, null, values);

        match.setId(id);

        return match;
    }

    //TODO: Iterate enum and do it that way
    public void saveMatch(Match match)
    {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_MATCH_KEY, match.getMatchKey());
        values.put(DatabaseHelper.COLUMN_MATCH_TYPE, match.getHeader().getMatchType());
        values.put(DatabaseHelper.COLUMN_MATCH_NUMBER, match.getHeader().getMatchNumber());
        values.put(DatabaseHelper.COLUMN_MATCH_TIME, match.getHeader().getMatchTime());

        List<String> redTeams = new ArrayList<>();
        List<String> blueTeams = new ArrayList<>();

        for (MatchTeamItem teamItem : match.getMatchTeams())
        {
            if (teamItem.getAlliance() == Alliance.RED)
                redTeams.add(teamItem.getTeam().getTeamNumber() + "|" + teamItem.getSide());
            else
                blueTeams.add(teamItem.getTeam().getTeamNumber() + "|" + teamItem.getSide());
        }

        values.put(DatabaseHelper.COLUMN_MATCH_RED_ALLIANCE, Arrays.toString(redTeams.toArray(new String[redTeams.size()])).replace("[", "").replace("]", ""));
        values.put(DatabaseHelper.COLUMN_MATCH_BLUE_ALLIANCE, Arrays.toString(blueTeams.toArray(new String[blueTeams.size()])).replace("[", "").replace("]", ""));

        db.update(DatabaseHelper.TABLE_MATCHES, values, DatabaseHelper.COLUMN_MATCH_ID + " = ?", new String[] { "" + match.getId() });
    }

    public List<Match> getAllMatches()
    {
        List<Match> matches = new ArrayList<>();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(DatabaseHelper.TABLE_MATCHES);

        Cursor cursor = builder.query(db, null, null, null, null, null, DatabaseHelper.TABLE_MATCHES + "." + DatabaseHelper.COLUMN_MATCH_ID + " ASC");
        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            Map<Integer, MatchTeamItem> matchTeams = new LinkedHashMap<>();

            for (String s : cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MATCH_RED_ALLIANCE)).split(", "))
            {
                String[] teamSide = s.split(Pattern.quote("|"));

                matchTeams.put(Integer.parseInt(teamSide[0]), new MatchTeamItem(activity.getTeam(Integer.parseInt(teamSide[0])), Alliance.RED, Side.fromText(teamSide[1]))); //Red Alliance
            }

            for (String s : cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MATCH_BLUE_ALLIANCE)).split(", "))
            {
                String[] teamSide = s.split(Pattern.quote("|"));

                matchTeams.put(Integer.parseInt(teamSide[0]), new MatchTeamItem(activity.getTeam(Integer.parseInt(teamSide[0])), Alliance.BLUE, Side.fromText(teamSide[1]))); //Blue Alliance
            }

            matches.add(new Match(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_MATCH_ID)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MATCH_KEY)), new MatchHeaderItem(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MATCH_TYPE)), cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_MATCH_NUMBER)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MATCH_TIME))), matchTeams));

            cursor.moveToNext();
        }

        cursor.close();

        return matches;
    }

    public CategoryItem addCategory(CategoryItem category)
    {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_CATEGORY_TEAM, category.getTeamId());
        values.put(DatabaseHelper.COLUMN_CATEGORY_LIST_ID, category.getCategoryId());
        values.put(DatabaseHelper.COLUMN_CATEGORY_SCORE, category.getScore());

        int id = (int) db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values);

        category.setId(id);

        return category;
    }

    public void deleteCategory(int catId)
    {
        db.delete(DatabaseHelper.TABLE_CATEGORIES, DatabaseHelper.COLUMN_CATEGORY_LIST_ID + " = ?", new String[]{"" + catId});
        db.delete(DatabaseHelper.TABLE_CATEGORIES_LIST, DatabaseHelper.COLUMN_CATEGORY_ITEM_ID + " = ?", new String[]{"" + catId});
    }

    public void saveCategory(CategoryItem category)
    {
        ContentValues values = new ContentValues();

        if (category.getId() != -1)
            values.put(DatabaseHelper.COLUMN_CATEGORY_ID, category.getId());

        values.put(DatabaseHelper.COLUMN_CATEGORY_TEAM, category.getTeamId());
        values.put(DatabaseHelper.COLUMN_CATEGORY_LIST_ID, category.getCategoryId());
        values.put(DatabaseHelper.COLUMN_CATEGORY_SCORE, category.getScore());

        if (category.getId() != -1)
        {
            int id = (int) db.insertWithOnConflict(DatabaseHelper.TABLE_CATEGORIES, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            category.setId(id);
        } else
        {
            int id = (int) db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values);

            category.setId(id);
        }
    }

    public int addCategoryItem(String name, int defaultScore)
    {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_CATEGORY_ITEM_NAME, name);
        values.put(DatabaseHelper.COLUMN_CATEGORY_ITEM_DEFAULT_SCORE, defaultScore);

        int id = (int) db.insert(DatabaseHelper.TABLE_CATEGORIES_LIST, null, values);

        return id;
    }

    public void populateCategoryItems()
    {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DatabaseHelper.TABLE_CATEGORIES_LIST);

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

        if (!hasOffenseCat) addCategoryItem("Offense", 0);
        if (!hasDefenseCat) addCategoryItem("Defense", 0);
    }

	public Map<Integer, Team> getAllTeams()
	{
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

		String query = DatabaseHelper.TABLE_TEAMS + " LEFT JOIN " + DatabaseHelper.TABLE_CATEGORIES + " ON " + DatabaseHelper.TABLE_TEAMS + "." + DatabaseHelper.COLUMN_TEAM_ID + " = " + DatabaseHelper.TABLE_CATEGORIES + "." + DatabaseHelper.COLUMN_CATEGORY_TEAM + " LEFT JOIN " + DatabaseHelper.TABLE_CATEGORIES_LIST + " ON " + DatabaseHelper.TABLE_CATEGORIES + "." + DatabaseHelper.COLUMN_CATEGORY_LIST_ID + " = " + DatabaseHelper.TABLE_CATEGORIES_LIST + "." + DatabaseHelper.COLUMN_CATEGORY_ITEM_ID;

		builder.setTables(query);

		Cursor cursor = builder.query(db, null, null, null, null, null, DatabaseHelper.TABLE_TEAMS + "." + DatabaseHelper.COLUMN_TEAM_NUMBER + " ASC");
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
