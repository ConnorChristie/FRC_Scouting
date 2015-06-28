package me.connor.frcscouting.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import me.connor.frcscouting.tabs.MatchesFragment;
import me.connor.frcscouting.tabs.TeamsFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter
{
	private Fragment[] tabs;

	public TabsPagerAdapter(FragmentManager fm, Fragment[] tabs)
	{
		super(fm);

		this.tabs = tabs;
	}

	@Override
	public Fragment getItem(int i)
	{
		return tabs[i];
	}

	@Override
	public int getCount()
	{
		return tabs.length;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return tabs[position].toString();
	}

    public MatchesFragment getMatchesFragment()
    {
        return (MatchesFragment) tabs[0];
    }

    public TeamsFragment getTeamsFragment()
    {
        return (TeamsFragment) tabs[1];
    }
}
