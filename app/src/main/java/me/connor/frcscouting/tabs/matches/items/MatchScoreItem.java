package me.connor.frcscouting.tabs.matches.items;

import android.view.LayoutInflater;
import android.view.View;

import me.connor.frcscouting.R;
import me.connor.frcscouting.listadapter.ListItem;

public class MatchScoreItem extends ListItem
{
    public MatchScoreItem()
    {
        super(R.layout.match_list_score_item);
    }

    public View populate(View view, LayoutInflater li)
    {
        if (view == null)
        {
            view = super.populate(view, li);
        }

        //TextView name = (TextView) view.findViewById(R.id.match_item_title);



        return view;
    }

    @Override
    public String toString()
    {
        return "Score: 20";
    }

    @Override
    public boolean isHeader()
    {
        return false;
    }
}