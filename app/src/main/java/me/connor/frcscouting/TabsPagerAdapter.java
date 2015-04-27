package me.connor.frcscouting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter
{
	public Fragment[] tabs;

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
}
