package me.connor.frcscouting.events;

public class Event implements Comparable<Event>
{
    private String name;
    private String key;

    public Event(String name, String key)
    {
        this.name = name;
        this.key = key;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public int compareTo(Event another)
    {
        return name.compareTo(another.getName());
    }
}
