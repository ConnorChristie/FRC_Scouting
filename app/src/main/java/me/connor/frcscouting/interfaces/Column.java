package me.connor.frcscouting.interfaces;

public interface Column<T>
{
    String getValue(T i);

    boolean isId();
}
