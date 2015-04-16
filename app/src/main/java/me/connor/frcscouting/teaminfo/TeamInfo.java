package me.connor.frcscouting.teaminfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import me.connor.frcscouting.R;
import me.connor.frcscouting.TabsPagerAdapter;
import me.connor.frcscouting.fragments.MatchesFragment;
import me.connor.frcscouting.fragments.TeamsFragment;
import me.connor.frcscouting.listadapter.ListAdapter;
import me.connor.frcscouting.listadapter.ListItem;
import me.connor.frcscouting.teamfragments.TeamInfoFragment;
import me.connor.frcscouting.teams.Team;

public class TeamInfo extends ActionBarActivity
{
	private Team team;

	private EditText teamName;
	private EditText teamNumber;

	private ListView statsList;
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
				new TeamInfoFragment()
		});

		mViewPager = (ViewPager) findViewById(R.id.team_info_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);


		/*
		((TextView) findViewById(R.id.team_name)).setText(team.getTeamName());
		((TextView) findViewById(R.id.team_number)).setText(team.getTeamNumber() + "");

		findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});

		teamName = (EditText) findViewById(R.id.team_name);
		teamNumber = (EditText) findViewById(R.id.team_number);

		teamName.setOnEditorActionListener(textEditDoneEvent);
		teamNumber.setOnEditorActionListener(textEditDoneEvent);

		itemsSections.add(new CategoryItem("Offense", 8));
		itemsSections.add(new CategoryItem("Defense", 4));
		itemsSections.add(new CategoryAddItem());

		statsList = (ListView) findViewById(R.id.statsList);
		statsList.setAdapter(new ListAdapter(this, R.layout.team_info_list_item, itemsSections));
		*/
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

	private TextView.OnEditorActionListener textEditDoneEvent = new TextView.OnEditorActionListener()
	{
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			if (actionId == EditorInfo.IME_ACTION_DONE)
			{
				findViewById(R.id.team_info_layout).requestFocus();

				InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				in.hideSoftInputFromWindow(teamName.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

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
