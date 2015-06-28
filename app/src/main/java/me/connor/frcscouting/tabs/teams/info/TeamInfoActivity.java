package me.connor.frcscouting.tabs.teams.info;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.karim.MaterialTabs;
import me.connor.frcscouting.MainActivity;
import me.connor.frcscouting.R;
import me.connor.frcscouting.adapters.TabsPagerAdapter;
import me.connor.frcscouting.listadapter.ListItem;
import me.connor.frcscouting.tabs.MatchesFragment;
import me.connor.frcscouting.tabs.TeamsFragment;
import me.connor.frcscouting.tabs.teams.Team;
import me.connor.frcscouting.tabs.teams.info.tabs.comments.TeamCommentsFragment;
import me.connor.frcscouting.tabs.teams.info.tabs.info.TeamInfoFragment;
import me.connor.frcscouting.tabs.teams.info.tabs.info.adapter.CategoryListAdapter;
import me.connor.frcscouting.tabs.teams.info.tabs.info.items.CategoryItem;

public class TeamInfoActivity extends ActionBarActivity implements ActionBar.TabListener
{
    private MainActivity activity;
	private Team team;

	private List<ListItem> itemsSections = new ArrayList<>();

	private TabsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_info);

        activity = MainActivity.getInstance();

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

        // Bind the tabs to the ViewPager
        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.team_info_material_tabs);
        tabs.setViewPager(mViewPager);

        /*
		mViewPager = (ViewPager) findViewById(R.id.team_info_pager);
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
        */

		findViewById(R.id.save_team).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				TeamInfoFragment frag = (TeamInfoFragment) mSectionsPagerAdapter.getItem(0);

				team.setTeamName(frag.teamName.getText().toString());
				team.setTeamNumber(Integer.parseInt(frag.teamNumber.getText().toString()));
				team.setComments(((EditText) findViewById(R.id.comments)).getText().toString());

				setTitle(team.getTeamName());
				getSupportActionBar().setSubtitle(team.getTeamNumber() + "");

                activity.getDatabase().saveTeam(team);
				List<CategoryItem> cats = new ArrayList<>();

				for (ListItem item : ((CategoryListAdapter) TeamInfoFragment.getStatsList().getAdapter()).getUpdatedStats())
				{
					if (item instanceof CategoryItem)
					{
                        activity.getDatabase().saveCategory((CategoryItem) item);

						cats.add((CategoryItem) item);
					}
				}

				team.setCategories(cats);

                activity.getPagerAdapter().getTeamsFragment().updateTeam(team);
                activity.getPagerAdapter().getMatchesFragment().updateMatches(team);

                new AlertDialog.Builder(v.getContext())
						.setTitle("Saved Team")
						.setMessage("Successfully saved team '" + team.getTeamName() + "'")
						.setNegativeButton("Ok", null)
						.show();
			}
		});

		findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();

				/*
				new AlertDialog.Builder(v.getContext())
						.setTitle("Cancel")
						.setMessage("Are you sure you don't want to save before canceling?")
						.setPositiveButton("Yes, Cancel", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								((TeamsFragment) MainActivity.getPagerAdapter().tabs[1]).updateList();

								finish();
							}
						})
						.setNegativeButton("No", null)
						.show();
						*/
			}
		});

		getSupportActionBar().setElevation(0);
		//getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//getSupportActionBar().addTab(getSupportActionBar().newTab().setText("Info").setTabListener(this));
		//getSupportActionBar().addTab(getSupportActionBar().newTab().setText("Comments").setTabListener(this));
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

		if (id == R.id.delete_team_action)
		{
			new AlertDialog.Builder(this)
					.setTitle("Delete Team")
					.setMessage("Are you sure you want to delete this team?")
					.setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
                            activity.getDatabase().deleteTeam(team);
                            activity.getPagerAdapter().getTeamsFragment().updateTeams();

							finish();
						}
					})
					.setNegativeButton("No", null)
					.show();

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
