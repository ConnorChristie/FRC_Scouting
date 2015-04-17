package me.connor.frcscouting.teaminfo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.connor.frcscouting.R;
import me.connor.frcscouting.TabsPagerAdapter;
import me.connor.frcscouting.listadapter.ListItem;
import me.connor.frcscouting.teamfragments.TeamCommentsFragment;
import me.connor.frcscouting.teamfragments.TeamInfoFragment;
import me.connor.frcscouting.teams.Team;

public class TeamInfo extends ActionBarActivity
{
	private Team team;

	private List<ListItem> itemsSections = new ArrayList<>();

	private TabsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_info);

		Intent callingIntent = getIntent();

		team = callingIntent.getParcelableExtra("teamData");

		setTitle(team.getTeamName());
		getSupportActionBar().setSubtitle(team.getTeamNumber() + "");

		mSectionsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), new Fragment[] {
				new TeamInfoFragment(),
				new TeamCommentsFragment()
		});

		mViewPager = (ViewPager) findViewById(R.id.team_info_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	public Team getTeam()
	{
		return team;
	}

	public List<ListItem> getItemsSections()
	{
		return itemsSections;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_team_info, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		if (id == R.id.action_settings)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public TextView.OnEditorActionListener textEditDoneEvent = new TextView.OnEditorActionListener()
	{
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			if (actionId == EditorInfo.IME_ACTION_DONE)
			{
				findViewById(R.id.team_info_layout).requestFocus();

				InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				in.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

				return true;
			}

			return false;
		}
	};

	public class MyPagerAdapter extends FragmentPagerAdapter
	{
		private final String[] TITLES = { "Info", "Statistics", "Comments" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			return new TeamInfoFragment();
		}

	}
}
