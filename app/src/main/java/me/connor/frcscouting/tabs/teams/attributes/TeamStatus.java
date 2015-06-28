package me.connor.frcscouting.tabs.teams.attributes;

import android.graphics.Color;

public enum TeamStatus
{
	GOOD("Good", "#ff07b100"),
	AVERAGE("Average", "#ffd7a900"),
	BAD("Bad", "#ffd00b00"),
	UNDEFINED("-", "#000000");

	private String title;
	private int color;

	private TeamStatus(String title, String color)
	{
		this.title = title;
		this.color = Color.parseColor(color);
	}

	public String getTitle()
	{
		return title;
	}

	public int getColor()
	{
		return color;
	}

	public static TeamStatus getFromInt(int score)
	{
		if (score > 0 && score <= 3)
		{
			return BAD;
		} else if (score > 3 && score <= 7)
		{
			return AVERAGE;
		} else if (score > 7 && score <= 10)
		{
			return GOOD;
		}

		return UNDEFINED;
	}
}
