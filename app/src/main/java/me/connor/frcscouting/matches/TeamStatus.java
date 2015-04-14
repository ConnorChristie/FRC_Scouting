package me.connor.frcscouting.matches;

import android.graphics.Color;

public enum TeamStatus
{
	GOOD("Good", "#ff07b100"),
	AVERAGE("Average", "#ffd7a900"),
	BAD("Bad", "#ffd00b00");

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
}
