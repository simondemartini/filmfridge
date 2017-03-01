package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import edu.uw.tacoma.tcss450.team4.filmfridge.authenticate.SignInActivity;
import edu.uw.tacoma.tcss450.team4.filmfridge.film.Film;

public class FilmActivity extends AppCompatActivity implements
    UpcomingListFragment.OnListFragmentInteractionListener,
        FilmDetailFragment.OnDetailFragmentInteractionListener {

    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private UpcomingListFragment mUpcomingListFragment;
    private MyListFragment mMyListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set up Hamburger menu
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        addDrawerItems();
        setupDrawer();

        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            mUpcomingListFragment = new UpcomingListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.film_fragment_container, mUpcomingListFragment)
                    .commit();
        }
    }

    /**
     * After onCreate. Update some things
     * @param savedInstanceState saved state
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Update drawer toggle icon
        mDrawerToggle.syncState();
    }

    /**
     * Activityi changes such as oritnetation changes, or showing keyboard
     * @param newConfig new config
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //update drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * A helper method to populate the nav drawer with options and the listeners
     */
    private void addDrawerItems() {
        final String[] nav = {"Upcoming", "My List", "Settings"};
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, nav);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = nav[position];
                switch(nav[position]) {
                    case("Upcoming"):
                        //replace fragment and close drawer
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.film_fragment_container, mUpcomingListFragment)
                                .addToBackStack(null)
                                .commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    case("My List"):
                        //replace fragment and close drawer
                        if(mMyListFragment == null) {
                            mMyListFragment = new MyListFragment();
                        }
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.film_fragment_container, mMyListFragment)
                                .addToBackStack(null)
                                .commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    default:
                        Toast.makeText(FilmActivity.this, "Selected: " + selected, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Create the nav drawer.
     */
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    public void onListFragmentInteraction(Film item) {
        FilmDetailFragment filmDetailFragment = new FilmDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(FilmDetailFragment.FILM_ITEM_SELECTED, item);
        filmDetailFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.film_fragment_container, filmDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_film_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            SharedPreferences sharedPreferences =
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                    .commit();

            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
            finish();
            return true;
        } else if(id == R.id.action_share) {
            //implemented in the fragment
            return false;
        } else if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetailFragmentInteraction(Uri uri) {

    }
}
