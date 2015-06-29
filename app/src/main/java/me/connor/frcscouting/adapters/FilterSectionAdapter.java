package me.connor.frcscouting.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.applidium.headerlistview.HeaderListView;
import com.applidium.headerlistview.SectionAdapter;

import java.util.ArrayList;
import java.util.List;

import me.connor.frcscouting.MainActivity;
import me.connor.frcscouting.tabs.matches.Match;
import me.connor.frcscouting.tabs.matches.attributes.Side;
import me.connor.frcscouting.tabs.matches.items.MatchHeaderItem;
import me.connor.frcscouting.tabs.matches.items.MatchTeamItem;
import me.connor.frcscouting.tabs.teams.info.TeamInfoActivity;

public class FilterSectionAdapter extends SectionAdapter implements Filterable
{
    private View view;
    private Fragment fragment;

    private List<Match> matches;
    private final Object mLock = new Object();

    private ArrayList<Match> mOriginalValues;
    private ArrayFilter mFilter;

    public FilterSectionAdapter(View view, Fragment fragment, HeaderListView listView, List<Match> matches)
    {
        super(listView);

        this.view = view;
        this.fragment = fragment;
        this.matches = matches;
    }

    @Override
    public int numberOfSections()
    {
        return matches.size();
    }

    @Override
    public int numberOfRows(int section)
    {
        return matches.get(section).getMatchTeams().size();
    }

    @Override
    public View getRowView(int section, int row, View convertView, ViewGroup parent)
    {
        MatchTeamItem item = (MatchTeamItem) getRowItem(section, row);

        return item.populate(convertView, (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent)
    {
        MatchHeaderItem item = matches.get(section).getHeader();

        return item.populate(convertView, (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    @Override
    public boolean hasSectionHeaderView(int section)
    {
        return true;
    }

    @Override
    public Object getRowItem(int section, int row)
    {
        Match match = matches.get(section);

        return match.getMatchTeams().get(row);
    }

    @Override
    public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id)
    {
        Intent teamInfoIntent = new Intent(fragment.getActivity(), TeamInfoActivity.class);
        teamInfoIntent.putExtra("teamData", ((MatchTeamItem) getRowItem(section, row)).getTeam());

        fragment.startActivity(teamInfoIntent);
    }

    @Override
    public boolean onRowItemLongClick(AdapterView<?> parent, View view, int section, int row, long id)
    {
        if (isSectionHeader((int) id)) return false;

        MatchTeamItem item = (MatchTeamItem) getRowItem(section, row);

        if (item.getSide() == Side.OFFENSE)
        {
            item.setSide(Side.DEFENSE);
        } else
        {
            item.setSide(Side.OFFENSE);
        }

        notifyDataSetChanged();

        Toast.makeText(view.getContext(), "Changed side to " + item.getSide(), Toast.LENGTH_SHORT).show();

        MainActivity.getInstance().getDatabase().save(matches.get(section));

        return true;
    }

    public Filter getFilter()
    {
        if (mFilter == null)
        {
            mFilter = new ArrayFilter();
        }

        return mFilter;
    }

    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */
    private class ArrayFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence prefix)
        {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null)
            {
                synchronized (mLock)
                {
                    mOriginalValues = new ArrayList<>(matches);
                }
            }

            if (prefix == null || prefix.length() == 0)
            {
                ArrayList<Match> list;
                synchronized (mLock)
                {
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else
            {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<Match> values;
                synchronized (mLock)
                {
                    values = new ArrayList<>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<Match> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++)
                {
                    final Match value = values.get(i);
                    final String valueText = value.toString().toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.contains(prefixString))
                    {
                        newValues.add(value);
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            //noinspection unchecked
            matches = (List<Match>) results.values;

            notifyDataSetChanged();
        }
    }
}
