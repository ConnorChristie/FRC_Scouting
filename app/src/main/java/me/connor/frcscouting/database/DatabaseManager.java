package me.connor.frcscouting.database;

import com.activeandroid.query.Select;

import java.util.List;

import me.connor.frcscouting.database.models.Match;

public class DatabaseManager
{
    public static List<Match> getMatches()
    {
        List<Match> matches = new Select().from(Match.class).execute();

        return matches;
    }
}
