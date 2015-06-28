package me.connor.frcscouting.tabs.matches.attributes;

import android.graphics.Color;

public enum Alliance
{
    RED(Color.parseColor("#158b0002")),
    BLUE(Color.parseColor("#1546ceff"));

    private int backgroundColor = 0;

    Alliance(int color)
    {
        backgroundColor = color;
    }

    public int getBackgroundColor()
    {
        return backgroundColor;
    }
}
