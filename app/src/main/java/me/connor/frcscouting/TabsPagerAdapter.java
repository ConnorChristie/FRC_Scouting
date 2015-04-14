package me.connor.frcscouting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import me.connor.frcscouting.fragments.MatchesFragment;
import me.connor.frcscouting.fragments.TeamsFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter
{
	public static String[] titles = new String[] { "Matches", "Teams" };

	public TabsPagerAdapter(FragmentManager fm)
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int i)
	{
		switch (i)
		{
			case 0:
				return new MatchesFragment();
			case 1:
				return new TeamsFragment();
		}

		return null;
	}

	@Override
	public int getCount()
	{
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return titles[position];
	}
}
