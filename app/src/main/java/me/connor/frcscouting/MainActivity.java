package me.connor.frcscouting;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;

import com.activeandroid.ActiveAndroid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import io.karim.MaterialTabs;
import me.connor.frcscouting.adapters.FilterSectionAdapter;
import me.connor.frcscouting.adapters.TabsPagerAdapter;
import me.connor.frcscouting.database.DatabaseDataSource;
import me.connor.frcscouting.database.DatabaseManager;
import me.connor.frcscouting.database.models.Match;
import me.connor.frcscouting.events.Event;
import me.connor.frcscouting.listadapter.ListAdapter;
import me.connor.frcscouting.notifications.android.NotificationManager;
import me.connor.frcscouting.tabs.MatchesFragment;
import me.connor.frcscouting.tabs.TeamsFragment;
import me.connor.frcscouting.tabs.teams.Team;
import me.connor.frcscouting.thebluealliance.api.EventsApi;
import me.connor.frcscouting.thebluealliance.api.MatchesAPI;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener
{
    private static MainActivity activity;

    private Map<Integer, Team> teams = new HashMap<>();
    private List<Event> eventList = new ArrayList<>();

    private ViewPager mViewPager;
    private TabsPagerAdapter mSectionsPagerAdapter;

    private DatabaseDataSource db;
    private NotificationManager notificationManager;

    private DatabaseManager databaseManager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveAndroid.initialize(this);

        activity = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        new EventsApi().execute();

        //Log.d("", "Delete Database: " + deleteDatabase("frc_scouting.db"));
        //sharedPreferences.edit().clear().apply();

        db = new DatabaseDataSource(this);
        db.open();

        db.populateCategoryItems();
        teams = db.getAllTeams();

        mSectionsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), new Fragment[]{
                new MatchesFragment(),
                new TeamsFragment()
        });

        mSectionsPagerAdapter.getMatchesFragment().setMatches(db.getAllMatches());

        // Initialize the ViewPager and set an adapter
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Bind the tabs to the ViewPager
        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.material_tabs);
        tabs.setViewPager(mViewPager);

        notificationManager = new NotificationManager();

        getSupportActionBar().setElevation(0);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    public List<Team> getTeams()
    {
        List<Team> list = new ArrayList<>();
        list.addAll(teams.values());

        return list;
    }

    public boolean hasTeam(int teamNum)
    {
        return teams.containsKey(teamNum);
    }

    public Team getTeam(int teamNum)
    {
        return teams.get(teamNum);
    }

    public Map<Integer, Team> getTeamsMap()
    {
        return teams;
    }

    public List<Event> getEventList()
    {
        return eventList;
    }

    public SharedPreferences getSharedPreferences()
    {
        return sharedPreferences;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            final SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

            searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
            {
                @Override
                public boolean onQueryTextChange(String query)
                {
                    ((FilterSectionAdapter) mSectionsPagerAdapter.getMatchesFragment().getMatchesList().getAdapter()).getFilter().filter(query);
                    ((ListAdapter) mSectionsPagerAdapter.getTeamsFragment().getTeamsList().getAdapter()).setFiltered(true, query);

                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query)
                {
                    searchView.clearFocus();

                    ((FilterSectionAdapter) mSectionsPagerAdapter.getMatchesFragment().getMatchesList().getAdapter()).getFilter().filter(query);
                    ((ListAdapter) mSectionsPagerAdapter.getTeamsFragment().getTeamsList().getAdapter()).setFiltered(true, query);

                    return true;
                }
            });
            searchView.setOnCloseListener(new SearchView.OnCloseListener()
            {
                @Override
                public boolean onClose()
                {
                    ((FilterSectionAdapter) mSectionsPagerAdapter.getMatchesFragment().getMatchesList().getAdapter()).getFilter().filter("");
                    ((ListAdapter) mSectionsPagerAdapter.getTeamsFragment().getTeamsList().getAdapter()).setFiltered(false, null);


                    //Search, first match header stays there after searching...


                    return false;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_add_team)
        {
            View promptView = getLayoutInflater().inflate(R.layout.dialog_add_team, null);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setView(promptView);

            final EditText teamName = (EditText) promptView.findViewById(R.id.teamName);
            final EditText teamNumber = (EditText) promptView.findViewById(R.id.teamNumber);

            dialogBuilder.setTitle("Add Team").setCancelable(true).setPositiveButton("Add", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    //Add team to database and add categories

                    boolean noConflict = true;
                    int teamNum = Integer.parseInt(teamNumber.getText().toString());

                    for (Team team : getTeams())
                    {
                        if (team.getTeamNumber() == teamNum)
                        {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Error")
                                    .setMessage("There is already a team with the team number '" + teamNum + "'")
                                    .setNegativeButton("Ok", null)
                                    .show();

                            noConflict = false;

                            break;
                        }
                    }

                    if (noConflict)
                    {
                        Team team = new Team(0, teamNum, teamName.getText().toString(), "");

                        db.addNewTeam(team, true);
                        teams.put(team.getTeamNumber(), team);

                        mSectionsPagerAdapter.getTeamsFragment().updateTeams();
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = dialogBuilder.create();

            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    teamName.clearFocus();
                    teamNumber.clearFocus();
                }
            });

            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            alertDialog.show();

            return true;
        } else if (id == R.id.action_sync_matches)
        {
            View promptView = getLayoutInflater().inflate(R.layout.dialog_sync_matches, null);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setView(promptView);

            final Spinner eventPicker = (Spinner) promptView.findViewById(R.id.eventPicker);
            final EditText teamNumber = (EditText) promptView.findViewById(R.id.teamNumber1);

            Collections.sort(eventList);
            ArrayAdapter<Event> eventData = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, eventList);

            eventPicker.setAdapter(eventData);

            dialogBuilder.setTitle("Sync Matches").setCancelable(true).setPositiveButton("Sync", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    teamNumber.clearFocus();

                    try
                    {
                        Event event = (Event) eventPicker.getSelectedItem();
                        int teamNum = Integer.parseInt(teamNumber.getText().toString());

                        Set<String> events = sharedPreferences.getStringSet("events", new TreeSet<String>());
                        events.add(event.getName() + "~" + event.getKey());

                        sharedPreferences.edit().putStringSet("events", events).apply();
                        sharedPreferences.edit().putString("event_key", event.getKey()).apply();
                        sharedPreferences.edit().putInt("team_number", teamNum).apply();

                        new MatchesAPI(MainActivity.this, event.getKey(), teamNum).execute();
                    } catch (NumberFormatException e)
                    {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Invalid team number entered, make sure it is a number.")
                                .setNegativeButton("Ok", null)
                                .show();
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    dialog.cancel();
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    teamNumber.clearFocus();
                }
            });

            dialogBuilder.create().show();

            return true;
        } else if (id == R.id.action_change_event)
        {
            View promptView = getLayoutInflater().inflate(R.layout.dialog_change_event, null);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setView(promptView);

            final Spinner eventPicker = (Spinner) promptView.findViewById(R.id.eventPicker);

            Set<String> eventsSet = sharedPreferences.getStringSet("events", new TreeSet<String>());
            List<Event> events = new ArrayList<>();

            for (String eventData : eventsSet)
            {
                String[] ev = eventData.split("~");

                events.add(new Event(ev[0], ev[1]));
            }

            Collections.sort(events);
            ArrayAdapter<Event> eventData = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, events);

            eventPicker.setAdapter(eventData);

            dialogBuilder.setTitle("Change Event").setCancelable(true).setPositiveButton("Change", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    String eventKey = ((Event) eventPicker.getSelectedItem()).getKey();

                    sharedPreferences.edit().putString("event_key", eventKey).apply();

                    //getPagerAdapter().getMatchesFragment().updateEventKey(eventKey);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    dialog.cancel();
                }
            });

            dialogBuilder.create().show();
        } else if (id == R.id.action_settings)
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

    public TabsPagerAdapter getPagerAdapter()
    {
        return mSectionsPagerAdapter;
    }

    public DatabaseDataSource getDatabase()
    {
        return db;
    }

    public static MainActivity getInstance()
    {
        return activity;
    }
}