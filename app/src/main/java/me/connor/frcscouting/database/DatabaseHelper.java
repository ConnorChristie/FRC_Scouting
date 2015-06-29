package me.connor.frcscouting.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.connor.frcscouting.database.models.Categories;
import me.connor.frcscouting.database.models.CategoriesList;
import me.connor.frcscouting.database.models.Matches;
import me.connor.frcscouting.database.models.Teams;

public class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "frc_scouting.db";
	public static final int DATABASE_VERSION = 14;

    public Model[] models;

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

        models = new Model[] {
                new Matches(),
                new Teams(),
                new Categories(),
                new CategoriesList()
        };
	}

    //TODO: Make models for the categories and cat list.
	@Override
	public void onCreate(SQLiteDatabase db)
	{
        for (Model model : models) db.execSQL(model.getCreateString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (oldVersion != newVersion)
		{
            for (Model model : models) db.execSQL("DROP TABLE IF EXISTS " + model.getTable());

			onCreate(db);
		}
	}
}
