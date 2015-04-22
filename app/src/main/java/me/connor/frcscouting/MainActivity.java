package me.connor.frcscouting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import me.connor.frcscouting.database.DatabaseDataSource;
import me.connor.frcscouting.fragments.MatchesFragment;
import me.connor.frcscouting.fragments.TeamsFragment;
import me.connor.frcscouting.teams.Team;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener
{
	private DatabaseDataSource db;

	private List<Team> teams;

	private TabsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = new DatabaseDataSource(this);
		db.open();

		/*
		db.saveCategory(new CategoryItem(1, 1, "Offense", 4));
		db.saveCategory(new CategoryItem(2, 1, "Defense", 8));
		db.saveCategory(new CategoryItem(3, 2, "Defense", 2));

		db.saveTeam(new Team(1, 4095, "Team RoXI", "They have a super tall defense arm."));
		db.saveTeam(new Team(2, 3648, "Marquette Warriors", "Great maneuverability"));
		db.saveTeam(new Team(3, 1642, "King Fishers", "The best offense of all time"));
		*/

		teams = db.getAllTeams();

		mSectionsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), new Fragment[] {
				new MatchesFragment(),
				new TeamsFragment()
		});

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{

			}

			@Override
			public void onPageSelected(int position)
			{
				getSupportActionBar().setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{

			}
		});

		getSupportActionBar().setElevation(0);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		getSupportActionBar().addTab(getSupportActionBar().newTab().setText("Matches").setTabListener(this));
		getSupportActionBar().addTab(getSupportActionBar().newTab().setText("Teams").setTabListener(this));
	}

	public List<Team> getTeams()
	{
		return teams;
	}

	public Team getTeam(int id)
	{
		for (Team team : teams)
		{
			if (team.getId() == id)
			{
				return team;
			}
		}

		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_matches, menu);
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

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{
		mViewPager.setCurrentItem(tab.getPosition(), true);
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{

	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{

	}
}