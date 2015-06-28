package me.connor.frcscouting.tabs.matches.attributes;

public enum Side
{
    OFFENSE("OFFENSE"),
    DEFENSE("DEFENSE");

    private String text = "";

    Side(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    public String toString()
    {
        return text;
    }

    public static Side fromText(String text)
    {
        for (Side side : Side.values())
        {
            if (side.getText().equalsIgnoreCase(text)) return side;
        }

        return Side.OFFENSE;
    }
}
